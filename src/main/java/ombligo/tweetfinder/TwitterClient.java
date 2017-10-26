package ombligo.tweetfinder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import twitter4j.*;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwitterClient {
    private final Map<Airport, BigInteger> mostRecentAirportTweetId = new ConcurrentHashMap<>();
    private final Twitter twitter = TwitterFactory.getSingleton();
    private final TweetToneDeterminer tweetToneDeterminer;

    public List<Tweet> getTweets(Airport airport) {
        log.info("getTweets() for {}", airport);
        Query query = queryFromAirport(airport);
        QueryResult result;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            log.error("Search failed", e);
            return  Collections.emptyList();
        }
        List<Tweet> list = new ArrayList<>();
        for (Status status : result.getTweets()) {
            Tweet tweet = statusToTweet(status);
            list.add(tweet);
            updateMostRecentId(airport, status);
        }
        return list;
    }

    private void updateMostRecentId(Airport airport, Status status) {

        BigInteger mostRecentId = mostRecentAirportTweetId.getOrDefault(airport, BigInteger.ZERO);
        BigInteger thisTweetId = BigInteger.valueOf(status.getId());

        mostRecentAirportTweetId.put(airport, mostRecentId.max(thisTweetId));

    }

    private Query queryFromAirport(Airport airport) {
        Query query = new Query("@Delta OR #Delta");
        query.setLang("en");
        query.count(100);
        query.geoCode(new GeoLocation(Double.valueOf(airport.getLatitude()),Double.valueOf(airport.getLongitude())), 25, "mi");
        long sinceId = mostRecentAirportTweetId.getOrDefault(airport, BigInteger.ZERO).longValue();
        log.info("Using sinceId: {}", sinceId);
        query.setSinceId(sinceId);
        return query;
    }

    private Tweet statusToTweet(Status status) {
        Tweet tweet = Tweet.builder()
                .link("https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId())
                .profilePic(status.getUser().getProfileImageURL())
                .retweetCount(status.getRetweetCount())
                .time(status.getCreatedAt())
                .user(status.getUser().getScreenName())
                .text(status.getText())
                .build();

          tweetToneDeterminer.setPositivity(tweet);
          return tweet;
    }

}

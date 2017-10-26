package ombligo.tweetfinder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TweetService {
    private final RedisService redisService;
    private final TwitterClient twitterClient;
    private final ObjectReader objectReader = initializeObjectReader();
    private final ObjectWriter objectWriter = initializeObjectWriter();

    private ObjectWriter initializeObjectWriter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writer().forType(Tweet.class);
    }

    private ObjectReader initializeObjectReader() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.reader().forType(new TypeReference<List<Airport>>(){});
    }

    public void cacheNew() {
        log.info("Caching new tweets");
        String json = redisService.get("airports");
        log.info("sample: {}", json.substring(0, 55));

        List<Airport> airports = null;
        try {
            airports = objectReader.readValue(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Airport airport : airports) {
            List<Tweet> tweetList = twitterClient.getTweets(airport);
            log.info("Found {} tweets for {}", tweetList.size(), airport.getCode());
            List<String> tweetListJson = tweetList.stream().map(value -> {
                try {
                    return objectWriter.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    log.error("FAILED to process json", e);
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            redisService.addToSet("airport::" + airport.getCode() + "::tweets", tweetListJson);
        }
    }

    private Airport findAtl(List<Airport> airports) {
        return airports.stream().filter(airport -> "ATL".equals(airport.getCode())).collect(Collectors.toList()).get(0);
    }
}

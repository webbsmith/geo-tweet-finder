package ombligo.tweetfinder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ScheduledTasks {

    private final TweetService tweetService;

    @Scheduled(cron = "0 */2 * * * *") // every two minutes
    public void cacheNewTweets() {
        tweetService.cacheNew();
    }
}

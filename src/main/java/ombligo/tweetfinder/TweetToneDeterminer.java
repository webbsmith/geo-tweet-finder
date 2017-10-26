package ombligo.tweetfinder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetToneDeterminer {

    private final Set<String> negativeWords = loadNegativeWords();
    private final Pattern nonWords = Pattern.compile("[^a-z-]+");

    private Set<String> loadNegativeWords() {
        String fileName = "/negative-words.txt";
        try {
            InputStream in = getClass().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            return reader.lines().collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("FAILED to load file " + fileName, e);
            throw new RuntimeException(e);
        }
    }

    public String findNegativeWord(String text) {
        try {
            String[] justWords = nonWords.matcher(text.toLowerCase()).replaceAll(",").split(",");
            for (String word : justWords) {
                if (negativeWords.contains(word)) {
                    log.info("Negative word, '{}', in tweet: {}", word, text);
                    return word;
                }
            }
        } catch (RuntimeException e) {
            log.error("Unexpected exception. Defaulting tweet to positive: {}", text, e);
        }
        return null;
    }

    public void setPositivity(Tweet tweet) {
        String negativeWord = findNegativeWord(tweet.getText());
        if (negativeWord == null) {
            tweet.setPositive(Boolean.TRUE);
            return;
        }
        tweet.setPositive(Boolean.FALSE);
        tweet.setNegativeWord(negativeWord);
    }
}

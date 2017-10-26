package ombligo.tweetfinder;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TweetToneDeterminerTest {
    @Test
    public void isPositive() throws Exception {
        TweetToneDeterminer tweetToneDeterminer = new TweetToneDeterminer();
        List<String> tweets = Arrays.asList(
                "@Delta cust serv said my status would be upgraded to silver, per company promotion, after tues flight. Gate agent saying otherwise pls help!",
                "@CodenameMinaLi @Delta Sorry, Mina\\xe2\\x80\\x94I'll untag you in the thread.\\",
                "When airlines (@Delta in this case) are unaware and unorganized to handle large teams that are flying with them, there is ZERO excuse",
                "@Delta allowed my 6 yr daughter to fly in a middle seat, btwn 2 grown men while I sat in a diff row. Great #delta",
                "@Delta you have the worst customer service/relations in the biz.. take my money and then disappear when there\\xe2\\x80\\x99s a problem.",
                "RT @PattyDahlgren: @IStepFunny @Delta Keep us posted. We\\xe2\\x80\\x99re with you cheering you on. #doitDelta! Disabled dying Puerto Rican\\xe2\\x80\\x99s are dependi\\xe2\\x80\\xa6"
        );
        for (String tweet : tweets) {
            tweetToneDeterminer.findNegativeWord(tweet); // todo - write real test
        }
    }

}
package ombligo.tweetfinder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {
    private String link;
    private String user;
    private String profilePic;
    private Date time;
    private Boolean positive;
    private Integer retweetCount;
    private String text;
    private String negativeWord;
}

package ombligo.tweetfinder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
    private String code;
    private String name;
    private String latitude;
    private String longitude;
    private String city;
}

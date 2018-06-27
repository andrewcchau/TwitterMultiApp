package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;


public class TwitterConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String consumerKey;

    @NotEmpty
    @JsonProperty
    private String consumerSecret;

    @NotEmpty
    @JsonProperty
    private String accessToken;

    @NotEmpty
    @JsonProperty
    private String accessTokenSecret;

    public String getConsumerKey(){
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }
}
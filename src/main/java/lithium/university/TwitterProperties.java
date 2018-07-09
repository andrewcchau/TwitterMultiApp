package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;


public class TwitterProperties {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;

    @JsonProperty("consumerKey")
    public Optional<String> getConsumerKey() {
        return Optional.ofNullable(consumerKey);
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    @JsonProperty("consumerSecret")
    public Optional<String> getConsumerSecret() {
        return Optional.ofNullable(consumerSecret);
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    @JsonProperty("accessToken")
    public Optional<String> getAccessToken() {
        return Optional.ofNullable(accessToken);
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("accessTokenSecret")
    public Optional<String> getAccessTokenSecret() {
        return Optional.ofNullable(accessTokenSecret);
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }
}

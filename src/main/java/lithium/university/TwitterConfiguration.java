package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;


public class TwitterConfiguration extends Configuration {
    private TwitterProperties twitterProperties;

    public TwitterConfiguration() { }

    @JsonProperty("Twitter")
    public TwitterProperties getTwitterProperties() {
        return twitterProperties;
    }

    public void setTwitterProperties(TwitterProperties twitterProperties) {
        this.twitterProperties = twitterProperties;
    }
}
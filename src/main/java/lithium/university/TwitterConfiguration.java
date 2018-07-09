package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.util.Optional;


public class TwitterConfiguration extends Configuration {
    private TwitterProperties twitterProperties;

    public TwitterConfiguration() { }

    @JsonProperty("Twitter")
    public Optional<TwitterProperties> getTwitterProperties() {
        return Optional.ofNullable(twitterProperties);
    }

    public void setTwitterProperties(TwitterProperties twitterProperties) {
        this.twitterProperties = twitterProperties;
    }
}
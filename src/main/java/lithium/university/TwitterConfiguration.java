package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TwitterConfiguration extends Configuration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private TwitterProperties twitterProperties;

    public TwitterConfiguration() {
        logger.debug("Created TwitterConfiguration object");
    }

    @JsonProperty("Twitter")
    public TwitterProperties getTwitterProperties() {
        return twitterProperties;
    }

    public void setTwitterProperties(TwitterProperties twitterProperties) {
        this.twitterProperties = twitterProperties;
    }
}
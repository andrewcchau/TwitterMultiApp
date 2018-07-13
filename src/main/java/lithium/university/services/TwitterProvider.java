package lithium.university.services;

import lithium.university.TwitterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.inject.Inject;
import javax.inject.Provider;

public class TwitterProvider implements Provider {
    private final Logger logger = LoggerFactory.getLogger(TwitterProvider.class);
    private TwitterProperties twitterProperties;

    @Inject public TwitterProvider() {}

    /*
    * This should ideally be called before a get() call in order to get a non-null Twitter object.
    * */
    public void setTwitterProperties(TwitterProperties twitterProperties) {
        this.twitterProperties = twitterProperties;
    }

    /*
    * Used primarily for testing purposes
    * */
    public TwitterProperties getTwitterProperties() {
        return twitterProperties;
    }

    @Override
    public Twitter get() {
        logger.info("Providing new Twitter object");
        if (twitterProperties == null) {
            return null;
        }
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
        cb.setOAuthConsumerKey(twitterProperties.getConsumerKey())
                .setOAuthConsumerSecret(twitterProperties.getConsumerSecret())
                .setOAuthAccessToken(twitterProperties.getAccessToken())
                .setOAuthAccessTokenSecret(twitterProperties.getAccessTokenSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        return twitterFactory.getInstance();
    }
}

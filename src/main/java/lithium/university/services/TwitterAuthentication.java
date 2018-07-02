package lithium.university.services;

import lithium.university.TwitterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuthentication {
    private static final TwitterAuthentication INSTANCE = new TwitterAuthentication();
    private final Logger logger = LoggerFactory.getLogger(TwitterAuthentication.class);
    private TwitterProperties twitterProperties;

    private TwitterAuthentication() {}

    public static TwitterAuthentication getInstance() {
        return INSTANCE;
    }

    public Twitter getAuthenticatedTwitter() {
        logger.debug("Re-authenticating Twitter credentials");
        if(twitterProperties == null) {
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

    public void setTwitterProperties(TwitterProperties twitterProperties) {
        this.twitterProperties = twitterProperties;
    }
}

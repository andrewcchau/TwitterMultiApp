package lithium.university;

import dagger.Module;
import dagger.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Module
public class TwitterProvider {
    private static final TwitterProvider INSTANCE = new TwitterProvider();
    private final Logger logger = LoggerFactory.getLogger(TwitterProvider.class);
    private TwitterProperties twitterProperties;

    @Provides
    public Twitter provideTwitter() {
        return INSTANCE.get();
    }

    public static TwitterProvider getInstance() {
        return INSTANCE;
    }

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

package lithium.university;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterApplication extends Application<TwitterConfiguration> {
    public static final int TWEET_TOTAL = 25;
    public static final int TWEET_LENGTH = 280;

    public static void main(String args[]) throws Exception {
        new TwitterApplication().run(args);
    }

    @Override
    public String getName(){
        return "MultiTwitterApp";
    }

    @Override
    public void initialize(Bootstrap<TwitterConfiguration> bootstrap){
        // nothing
    }

    @Override
    public void run(TwitterConfiguration config, Environment environment){
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
        cb.setDebugEnabled(config.getDebug())
                .setOAuthConsumerKey(config.getConsumerKey())
                .setOAuthConsumerSecret(config.getConsumerSecret())
                .setOAuthAccessToken(config.getAccessToken())
                .setOAuthAccessTokenSecret(config.getAccessTokenSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        final Twitter twitter = twitterFactory.getInstance();
        final TwitterHealthCheck healthCheck = new TwitterHealthCheck();
        final TwitterResource resource = new TwitterResource();

        environment.healthChecks().register("twitter", healthCheck);
        environment.jersey().register(resource);
    }
}
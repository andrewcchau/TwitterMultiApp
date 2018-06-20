package lithium.university;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import twitter4j.User;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import javax.ws.rs.core.Response;


public class TwitterApplication extends Application<TwitterConfiguration> {
    private static final int TWEET_TOTAL = 25;

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
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        final Twitter twitter = twitterFactory.getInstance();
        final TwitterHealthCheck healthCheck = new TwitterHealthCheck(twitter);
        final TwitterResource resource = new TwitterResource(twitter, TWEET_TOTAL, healthCheck);

        environment.healthChecks().register("twitter", healthCheck);
        environment.jersey().register(resource);
    }
}
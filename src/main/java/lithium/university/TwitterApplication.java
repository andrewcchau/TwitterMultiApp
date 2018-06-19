package lithium.university;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterApplication extends Application<TwitterConfiguration> {
    private static final int TWEET_TOTAL = 30;

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
        final TwitterResource resource = new TwitterResource(config.getTemplate(), twitter, TWEET_TOTAL);
        final TwitterHealthCheck healthCheck = new TwitterHealthCheck(config.getTemplate());

        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }
}
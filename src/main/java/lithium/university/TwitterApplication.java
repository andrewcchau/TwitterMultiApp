package lithium.university;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TwitterApplication extends Application<TwitterConfiguration> {
    private final Logger LOGGER = LoggerFactory.getLogger(TwitterApplication.class);
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
        LOGGER.debug("Starting up Twitter Application.");
        final TwitterHealthCheck healthCheck = new TwitterHealthCheck();
        final TwitterResource resource = new TwitterResource(config.getTwitterProperties());

        environment.healthChecks().register("twitter", healthCheck);
        environment.jersey().register(resource);
    }
}
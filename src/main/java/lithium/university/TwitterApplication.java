package lithium.university;

import dagger.Component;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lithium.university.resources.TwitterResource;
import lithium.university.services.TwitterService;

import javax.inject.Singleton;


public class TwitterApplication extends Application<TwitterConfiguration> {
    public static final int TWEET_TOTAL = 25;
    public static final int TWEET_LENGTH = 280;

    @Singleton
    @Component(modules = TwitterService.class)
    interface TwitterSetUp {
        TwitterResource resource();
        TwitterHealthCheck healthCheck();
    }

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
        TwitterSetUp setUp = lithium.university.DaggerTwitterApplication_TwitterSetUp.builder().build();
        setUp.resource().registerProperties(config.getTwitterProperties().get());

        environment.healthChecks().register("twitter", setUp.healthCheck());
        environment.jersey().register(setUp.resource());
    }
}
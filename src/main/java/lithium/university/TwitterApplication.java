package lithium.university;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lithium.university.resources.TwitterResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;


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
        /* Twitter App */
        TwitterSetUp setUp = lithium.university.DaggerTwitterSetUp
                                .builder()
                                .twitterProvider(new TwitterProvider(config.getTwitterProperties().get()))
                                .build();
        TwitterResource resource = setUp.resource();

        /* Configure CORS */
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "http://localhost:9000");
        cors.setInitParameter("allowedHeaders", "Origin");
        cors.setInitParameter("allowedMethods", "GET,POST");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        environment.healthChecks().register("twitter", setUp.healthCheck());
        environment.jersey().register(resource);
    }
}
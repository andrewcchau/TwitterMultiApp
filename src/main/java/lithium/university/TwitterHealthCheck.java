package lithium.university;

import com.codahale.metrics.health.HealthCheck;
import twitter4j.*;

public class TwitterHealthCheck extends HealthCheck {
    private final Twitter twitter;
    private User user;

    public TwitterHealthCheck(Twitter twitter){
        this.twitter = twitter;
    }

    @Override
    protected Result check() throws Exception {
        try{
            User user = twitter.verifyCredentials();
        }catch(Exception e){
            e.printStackTrace();
            return Result.unhealthy("Can't reach twitter due to faulty authentication!");
        }

        return Result.healthy();
    }

    public boolean isHealthy(){
        System.out.println("INFO  ------------------------- Verifying twitter authentication key and secrets");

        try{
            user = twitter.verifyCredentials();
        }catch(Exception e){
            System.out.println("ERROR: Twitter authentication failed! Please check your consumer and access key / secrets!");
            return false;
        }

        return true;
    }
}
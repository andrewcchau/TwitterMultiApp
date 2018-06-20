package lithium.university;

import com.codahale.metrics.health.HealthCheck;
import twitter4j.*;

public class TwitterHealthCheck extends HealthCheck {
    private final Twitter twitter;

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
}
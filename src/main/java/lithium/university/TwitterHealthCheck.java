package lithium.university;

import com.codahale.metrics.health.HealthCheck;
import twitter4j.*;

public class TwitterHealthCheck extends HealthCheck {
    private final Twitter twitter;
    private User user;
    private boolean health;

    public TwitterHealthCheck(Twitter twitter){
        this.twitter = twitter;
        health = false;
    }

    @Override
    protected Result check() throws Exception {
        try{
            User user = twitter.verifyCredentials();
        }catch(Exception e){
            e.printStackTrace();
            health = false;
            return Result.unhealthy("Can't reach twitter due to faulty authentication!");
        }

        health = true;
        return Result.healthy();
    }

    public boolean getHealth(){
        return health;
    }

    public void setHealth(boolean value){
        health = value;
    }
}
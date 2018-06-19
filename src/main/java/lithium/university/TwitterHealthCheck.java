package lithium.university;

import com.codahale.metrics.health.HealthCheck;

public class TwitterHealthCheck extends HealthCheck {
    private final String template;

    public TwitterHealthCheck(String template){
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        final String tweet = String.format(template, "TEST");
        if(!tweet.contains("TEST)")){
            return Result.unhealthy("Tweet doesn't include a name");
        }

        return Result.healthy();
    }
}
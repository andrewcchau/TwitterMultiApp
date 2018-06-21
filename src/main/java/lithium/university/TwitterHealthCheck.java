package lithium.university;

import com.codahale.metrics.health.HealthCheck;

public class TwitterHealthCheck extends HealthCheck {
    public TwitterHealthCheck(){ }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
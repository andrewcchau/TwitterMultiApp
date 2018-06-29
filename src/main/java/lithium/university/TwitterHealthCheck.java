package lithium.university;

import com.codahale.metrics.health.HealthCheck;

public class TwitterHealthCheck extends HealthCheck {
    public TwitterHealthCheck() { }

    @Override
    protected Result check() {
        return Result.healthy();
    }
}
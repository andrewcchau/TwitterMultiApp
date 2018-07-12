package lithium.university;

import com.codahale.metrics.health.HealthCheck;

import javax.inject.Inject;

public class TwitterHealthCheck extends HealthCheck {
    @Inject
    public TwitterHealthCheck() { }

    @Override
    protected Result check() {
        return Result.healthy();
    }
}
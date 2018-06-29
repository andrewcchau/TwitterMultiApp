package lithium.university;

import com.codahale.metrics.health.HealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterHealthCheck extends HealthCheck {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TwitterHealthCheck(){
        logger.debug("Created TwitterHealthCheck object");
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
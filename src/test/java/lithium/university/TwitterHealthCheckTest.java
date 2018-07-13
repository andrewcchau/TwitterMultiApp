package lithium.university;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TwitterHealthCheckTest {
    @Test
    public void testHealth() {
        TwitterHealthCheck twitterHealthcheck = new TwitterHealthCheck();
        assertEquals(HealthCheck.Result.healthy().isHealthy(), twitterHealthcheck.check().isHealthy());
    }
}

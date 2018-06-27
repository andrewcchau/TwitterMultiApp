package lithium.university;

import com.codahale.metrics.health.HealthCheck;
import org.junit.Assert;
import org.junit.Test;

public class TwitterHealthCheckTest {
    @Test
    public void testHealth() throws Exception {
        TwitterHealthCheck twitterHealthcheck = new TwitterHealthCheck();
        Assert.assertEquals(HealthCheck.Result.healthy().isHealthy(), twitterHealthcheck.check().isHealthy());
    }
}

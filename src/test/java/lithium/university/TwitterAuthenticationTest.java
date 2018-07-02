package lithium.university;

import lithium.university.services.TwitterAuthentication;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TwitterAuthenticationTest {
    private TwitterAuthentication twitterAuthentication;
    private String message = "SOME MESSAGE";

    @Before
    public void init() {
        twitterAuthentication = TwitterAuthentication.getInstance();
    }

    @Test
    public void testAuthenticationReturnNull() {
        twitterAuthentication.setTwitterProperties(null);
        Assert.assertNull(twitterAuthentication.getAuthenticatedTwitter());
    }

    @Test
    public void testAuthenticationReturnNonNull() {
        TwitterProperties twitterProperties = Mockito.mock(TwitterProperties.class);
        twitterAuthentication.setTwitterProperties(twitterProperties);
        Assert.assertNotNull(twitterAuthentication.getAuthenticatedTwitter());
    }
}

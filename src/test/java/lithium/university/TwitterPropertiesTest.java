package lithium.university;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TwitterPropertiesTest {
    private TwitterProperties twitterProperties;
    private final String MESSAGE = "THIS IS A TEST MESSAGE";

    @Before
    public void init() {
        twitterProperties = new TwitterProperties();
    }

    @Test
    public void testPropertiesGetAndSetConsumerKey() {
        twitterProperties.setConsumerKey(MESSAGE);
        assertEquals(MESSAGE, twitterProperties.getConsumerKey());
    }

    @Test
    public void testPropertiesGetAndSetConsumerSecret() {
        twitterProperties.setConsumerSecret(MESSAGE);
        assertEquals(MESSAGE, twitterProperties.getConsumerSecret());
    }

    @Test
    public void testPropertiesGetAndSetAccessToken() {
        twitterProperties.setAccessToken(MESSAGE);
        assertEquals(MESSAGE, twitterProperties.getAccessToken());
    }

    @Test
    public void testPropertiesGetAndSetAccessTokenSecret() {
        twitterProperties.setAccessTokenSecret(MESSAGE);
        assertEquals(MESSAGE, twitterProperties.getAccessTokenSecret());
    }
}

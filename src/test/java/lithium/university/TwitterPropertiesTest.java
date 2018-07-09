package lithium.university;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TwitterPropertiesTest {
    private TwitterProperties twitterProperties;
    private final String MESSAGE = "THIS IS A TEST MESSAGE";

    @Before
    public void init(){
        twitterProperties = new TwitterProperties();
    }

    @Test
    public void testPropertiesGetAndSetConsumerKey(){
        twitterProperties.setConsumerKey(MESSAGE);
        Assert.assertEquals(MESSAGE, twitterProperties.getConsumerKey().orElseThrow(NullPointerException::new));
    }

    @Test
    public void testPropertiesGetAndSetConsumerSecret(){
        twitterProperties.setConsumerSecret(MESSAGE);
        Assert.assertEquals(MESSAGE, twitterProperties.getConsumerSecret().orElseThrow(NullPointerException::new));
    }

    @Test
    public void testPropertiesGetAndSetAccessToken(){
        twitterProperties.setAccessToken(MESSAGE);
        Assert.assertEquals(MESSAGE, twitterProperties.getAccessToken().orElseThrow(NullPointerException::new));
    }

    @Test
    public void testPropertiesGetAndSetAccessTokenSecret(){
        twitterProperties.setAccessTokenSecret(MESSAGE);
        Assert.assertEquals(MESSAGE, twitterProperties.getAccessTokenSecret().orElseThrow(NullPointerException::new));
    }
}

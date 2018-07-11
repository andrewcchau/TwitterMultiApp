package lithium.university;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TwitterConfigurationTest {
    private TwitterConfiguration twitterConfiguration;
    private TwitterProperties twitterProperties;
    private final String MESSAGE = "Some consumer key";

    @Before
    public void init(){
        twitterConfiguration = new TwitterConfiguration();
        twitterProperties = Mockito.mock(TwitterProperties.class);
        Mockito.when(twitterProperties.getConsumerKey()).thenReturn(MESSAGE);
    }

    @Test
    public void testConfigurationGetAndSetTwitter(){
        twitterConfiguration.setTwitterProperties(twitterProperties);
        Assert.assertEquals(MESSAGE, twitterConfiguration.getTwitterProperties().get().getConsumerKey());
    }
}

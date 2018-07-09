package lithium.university;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class TwitterConfigurationTest {
    private TwitterConfiguration twitterConfiguration;
    private TwitterProperties twitterProperties;
    private final String MESSAGE = "Some consumer key";

    @Before
    public void init(){
        twitterConfiguration = new TwitterConfiguration();
        twitterProperties = Mockito.mock(TwitterProperties.class);
        Mockito.<Optional>when(twitterProperties.getConsumerKey()).thenReturn(Optional.of(MESSAGE));
    }

    @Test
    public void testConfigurationGetAndSetTwitter(){
        twitterConfiguration.setTwitterProperties(twitterProperties);
        Assert.assertEquals(MESSAGE, twitterConfiguration.getTwitterProperties().get().getConsumerKey().get());
    }
}

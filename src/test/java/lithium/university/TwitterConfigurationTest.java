package lithium.university;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterConfigurationTest {
    private TwitterConfiguration twitterConfiguration;
    private TwitterProperties twitterProperties;
    private final String MESSAGE = "Some consumer key";

    @Before
    public void init() {
        twitterConfiguration = new TwitterConfiguration();
        twitterProperties = mock(TwitterProperties.class);
        when(twitterProperties.getConsumerKey()).thenReturn(MESSAGE);
    }

    @Test
    public void testConfigurationGetAndSetTwitter() {
        twitterConfiguration.setTwitterProperties(twitterProperties);
        assertEquals(MESSAGE, twitterConfiguration.getTwitterProperties().get().getConsumerKey());
    }
}

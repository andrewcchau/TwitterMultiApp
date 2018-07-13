package lithium.university;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterProviderTest {
    private TwitterProvider twitterProviderTest;
    private TwitterProperties twitterPropertiesTest;

    @Before
    public void init() {
        twitterPropertiesTest = mock(TwitterProperties.class);
        twitterProviderTest = new TwitterProvider(twitterPropertiesTest);
    }

    @Test
    public void testSetterAndGetterProperties() {
        String key = "Test Key";

        when(twitterPropertiesTest.getConsumerKey()).thenReturn(key);

        twitterProviderTest.setTwitterProperties(twitterPropertiesTest);
        assertNotNull(twitterProviderTest.getTwitterProperties().getConsumerKey());
        assertEquals(key, twitterProviderTest.getTwitterProperties().getConsumerKey());
    }

    @Test
    public void testNonNullGet() {
        String key = "Test Key";

        when(twitterPropertiesTest.getAccessTokenSecret()).thenReturn(key);
        when(twitterPropertiesTest.getAccessToken()).thenReturn(key);
        when(twitterPropertiesTest.getConsumerSecret()).thenReturn(key);
        when(twitterPropertiesTest.getConsumerKey()).thenReturn(key);

        twitterProviderTest.setTwitterProperties(twitterPropertiesTest);
        assertNotNull(twitterProviderTest.get());
        assertNotNull(twitterProviderTest.provideTwitter());
        assertEquals(twitterProviderTest.get(), twitterProviderTest.provideTwitter());
    }

    @Test
    public void testNullGet() {
        twitterProviderTest.setTwitterProperties(null);
        assertNull(twitterProviderTest.get());
    }
}

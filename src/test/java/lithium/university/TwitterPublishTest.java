package lithium.university;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


@RunWith(MockitoJUnitRunner.class)
public class TwitterPublishTest {
    @Mock
    private Twitter twitterTest;

    @InjectMocks
    private TwitterPublish twitterPublishTest;

    @Before
    public void init() throws TwitterException {
        /*General status message for twitterPublish behavior*/
        Status s = Mockito.mock(Status.class);
        Mockito.when(s.getText()).thenReturn("General status message for testing.");
        Mockito.when(twitterTest.updateStatus(Mockito.anyString())).thenReturn(s);
    }

    private String generateStringLength(int charLength) {
        return StringUtils.repeat("!", charLength);
    }

    @Test
    public void testPublishCharLengthZero() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, generateStringLength(0), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(false, publishTest);
    }

    @Test
    public void testPublishCharLengthOver() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, generateStringLength(TwitterApplication.TWEET_LENGTH + 1), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(false, publishTest);
    }

    @Test
    public void testPublishCharLengthUnder() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, generateStringLength(TwitterApplication.TWEET_LENGTH - 1), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(true, publishTest);
    }

    @Test
    public void testPublishCharLengthEqual() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, generateStringLength(TwitterApplication.TWEET_LENGTH), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(true, publishTest);
    }
}

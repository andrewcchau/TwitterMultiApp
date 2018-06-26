package lithium.university;

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

    private String genStringLength(int charLength) {
        String ret = "";
        for (int i = 0; i < charLength; i++) {
            ret += "!";
        }
        return ret;
    }

    @Test
    public void testPublishCharLengthZero() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(0), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(false, publishTest);
        System.out.println("testPublishCharLengthZero pass");
    }

    @Test
    public void testPublishCharLengthOver() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(TwitterApplication.TWEET_LENGTH + 1), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(false, publishTest);
        System.out.println("testPublishCharLengthOver pass");
    }

    @Test
    public void testPublishCharLengthUnder() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(TwitterApplication.TWEET_LENGTH - 1), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(true, publishTest);
        System.out.println("testPublishCharLengthUnder pass");
    }

    @Test
    public void testPublishCharLengthEqual() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(TwitterApplication.TWEET_LENGTH), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(true, publishTest);
        System.out.println("testPublishLengthEqual pass");
    }
}

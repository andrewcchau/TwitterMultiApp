package lithium.university;

import lithium.university.services.TwitterServices;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServicesTest {
    @Mock
    private Twitter twitterTest;

    @InjectMocks
    private TwitterServices twitterServicesTest;

    private Status mockStatus(String message){
        Status s = Mockito.mock(Status.class);
        Mockito.when(s.getText()).thenReturn(message);
        return s;
    }

    private String mockMessage = "General status message for testing";

    private Status mockStatus() {
        return mockStatus("This is a mocked status!");
    }

    private String generateStringLength(int charLength) {
        return StringUtils.repeat("!", charLength);
    }

    @Before
    public void init() throws TwitterException {
        /*General status message for twitterPublish behavior*/
        Status s = Mockito.mock(Status.class);
        Mockito.when(s.getText()).thenReturn(mockMessage);
        Mockito.when(twitterTest.updateStatus(Mockito.anyString())).thenReturn(s);
    }

    @Test
    public void testPostCharLengthZero() throws TwitterException {
        Status publishTest = null;
        publishTest = twitterServicesTest.postToTwitter(twitterTest, generateStringLength(0), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(null, publishTest);
    }

    @Test
    public void testPostCharLengthOver() throws TwitterException {
        Status publishTest = null;
        publishTest = twitterServicesTest.postToTwitter(twitterTest, generateStringLength(TwitterApplication.TWEET_LENGTH + 1), TwitterApplication.TWEET_LENGTH);
        Assert.assertEquals(null, publishTest);
    }

    @Test
    public void testPostCharLengthUnder() throws TwitterException {
        Status publishTest = null;
        publishTest = twitterServicesTest.postToTwitter(twitterTest, generateStringLength(TwitterApplication.TWEET_LENGTH - 1), TwitterApplication.TWEET_LENGTH);
        Assert.assertNotNull(publishTest);
        Assert.assertEquals(mockMessage, publishTest.getText());
    }

    @Test
    public void testPostCharLengthEqual() throws TwitterException {
        Status publishTest = null;
        publishTest = twitterServicesTest.postToTwitter(twitterTest, generateStringLength(TwitterApplication.TWEET_LENGTH), TwitterApplication.TWEET_LENGTH);
        Assert.assertNotNull(publishTest);
        Assert.assertEquals(mockMessage, publishTest.getText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveNegative() throws TwitterException {
        List<Status> l = twitterServicesTest.retrieveFromTwitter(twitterTest, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveNothing() throws TwitterException {
        List<Status> l = twitterServicesTest.retrieveFromTwitter(twitterTest, 0);
    }

    @Test
    public void testRetrieveSomething() throws TwitterException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus());
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterServicesTest.retrieveFromTwitter(twitterTest, 1);
        Assert.assertEquals(1, l.size());
        Assert.assertEquals(mockStatus().getText(), l.get(0).getText());
    }

    @Test
    public void testRetrieveMany() throws TwitterException {
        int size = 10;
        String testMessage = "Many num ";
        ResponseList<Status> fakeList = new FakeResponseList<>();
        for(int i = 0; i < size; i++){
            fakeList.add(mockStatus(testMessage + i));
        }
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterServicesTest.retrieveFromTwitter(twitterTest, size);
        Assert.assertEquals(size, l.size());
        for(int i = 0; i < size; i++){
            Assert.assertEquals(testMessage + i, l.get(i).getText());
        }
    }

    @Test
    public void testAuthenticationReturnNull() {
        twitterServicesTest.setTwitterProperties(null);
        Assert.assertNull(twitterServicesTest.getAuthenticatedTwitter());
    }

    @Test
    public void testAuthenticationReturnNonNull() {
        TwitterProperties twitterProperties = Mockito.mock(TwitterProperties.class);
        twitterServicesTest.setTwitterProperties(twitterProperties);
        Assert.assertNotNull(twitterServicesTest.getAuthenticatedTwitter());
    }
}

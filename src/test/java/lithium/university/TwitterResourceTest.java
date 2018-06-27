package lithium.university;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class TwitterResourceTest {
    private TwitterRetrieve twitterRetrieveTest;
    private TwitterPublish twitterPublishTest;
    private TwitterResource twitterResourceTest;

    @Before
    public void init() {
        twitterResourceTest = new TwitterResource(new TwitterConfiguration());
        twitterRetrieveTest = Mockito.mock(TwitterRetrieve.class);
        twitterPublishTest = Mockito.mock(TwitterPublish.class);
        twitterResourceTest = new TwitterResource(twitterRetrieveTest, twitterPublishTest);
    }

    private Status mockStatus() {
        Status s = Mockito.mock(Status.class);
        Mockito.when(s.getText()).thenReturn("This is a mocked status!");
        return s;
    }

    @Test
    public void testResourceGetTimeline() throws TwitterException {
        List<Status> fakeList = new ArrayList<>();
        fakeList.add(mockStatus());

        Mockito.when(twitterRetrieveTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenReturn(fakeList);

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(1, ((List<Status>) ((Tweet) response.getEntity()).getContent()).size());
        Assert.assertEquals(mockStatus().getText(), ((List<Status>) ((Tweet) response.getEntity()).getContent()).get(0).getText());
    }

    @Test
    public void testResourceGetErrorHandling() throws TwitterException {
        Mockito.when(twitterRetrieveTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourcePost() throws TwitterException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterPublishTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.successMessage(message), response.getEntity());
    }

    @Test
    public void testResourcePostLengthError() throws TwitterException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterPublishTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(false);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.errorLengthMessage(), response.getEntity());
    }

    @Test
    public void testResourcePostLengthZeroError() throws TwitterException {
        String message = "";

        Mockito.when(twitterPublishTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(false);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.errorZeroMessage(), response.getEntity());
    }

    @Test
    public void testResourcePostErrorHandling() throws TwitterException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterPublishTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }
}

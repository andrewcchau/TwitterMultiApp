package lithium.university;

import lithium.university.resources.TwitterResource;
import lithium.university.services.TwitterService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class TwitterResourceTest {
    private TwitterResource twitterResourceTest;
    private TwitterService twitterServiceTest;
    private String errorLength = "Message length should not exceed 280 characters";
    private String errorZero = "Message length must be greater than 0";

    @Before
    public void init() {
        twitterResourceTest = new TwitterResource(new TwitterProperties());
        twitterServiceTest = Mockito.mock(TwitterService.class);
        twitterResourceTest = new TwitterResource(twitterServiceTest);
        Mockito.when(twitterServiceTest.getAuthenticatedTwitter()).thenReturn(Mockito.mock(Twitter.class));
    }

    private Status mockStatus() {
        Status s = Mockito.mock(Status.class);
        return s;
    }

    @Test
    public void testResourceGetTimeline() throws TwitterException {
        List<Status> fakeList = new ArrayList<>();
        fakeList.add(mockStatus());

        Mockito.when(twitterServiceTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenReturn(fakeList);
        Mockito.when(twitterServiceTest.getAuthenticatedTwitter()).thenReturn(Mockito.mock(Twitter.class));

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(1, ((List<Status>) ((Tweet) response.getEntity()).getContent()).size());
        Assert.assertEquals(mockStatus().getText(), ((List<Status>) ((Tweet) response.getEntity()).getContent()).get(0).getText());
    }

    @Test
    public void testResourceGetErrorHandling() throws TwitterException {
        Mockito.when(twitterServiceTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourcePost() throws TwitterException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(mockStatus());

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.successMessage(message), response.getEntity());
    }

    @Test
    public void testResourcePostLengthError() throws TwitterException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new TwitterException(errorLength));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorLength, response.getEntity());
    }

    @Test
    public void testResourcePostLengthZeroError() throws TwitterException {
        String message = "";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new TwitterException(errorZero));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorZero, response.getEntity());
    }

    @Test
    public void testResourcePostErrorHandling() throws TwitterException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new TwitterException(twitterResourceTest.getErrorMessage()));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourcePostNullMessage() {
        Response response = twitterResourceTest.postTweet(null);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getMessageFormError(), response.getEntity());
    }
}

package lithium.university;

import lithium.university.models.TwitterPost;
import lithium.university.resources.TwitterResource;
import lithium.university.services.TwitterService;
import lithium.university.services.TwitterServiceException;
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

    private TwitterPost mockPost() {
        TwitterPost tp = Mockito.mock(TwitterPost.class);
        return tp;
    }

    @Test
    public void testResourceGetTimeline() throws TwitterException {
        List<TwitterPost> fakeList = new ArrayList<>();
        fakeList.add(mockPost());

        Mockito.when(twitterServiceTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenReturn(fakeList);
        Mockito.when(twitterServiceTest.getAuthenticatedTwitter()).thenReturn(Mockito.mock(Twitter.class));

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(1, ((List<TwitterPost>) ((Tweet) response.getEntity()).getContent()).size());
        Assert.assertEquals(mockPost().getTwitterMessage(), ((List<TwitterPost>) ((Tweet) response.getEntity()).getContent()).get(0).getTwitterMessage());
    }

    @Test
    public void testResourceGetErrorHandling() throws TwitterException {
        Mockito.when(twitterServiceTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourcePost() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(mockStatus());

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.successMessage(message), response.getEntity());
    }

    @Test
    public void testResourcePostLengthError() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";
        String errorLength = "Message length should not exceed 280 characters";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new TwitterServiceException(errorLength));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorLength, response.getEntity());
    }

    @Test
    public void testResourcePostLengthZeroError() throws TwitterException, TwitterServiceException {
        String message = "";
        String errorZero = "Message length must be greater than 0";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new TwitterServiceException(errorZero));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorZero, response.getEntity());
    }

    @Test
    public void testResourcePostErrorHandling() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenThrow(new TwitterException(twitterResourceTest.getErrorMessage()));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourcePostNullMessage() throws TwitterException, TwitterServiceException{
        String errorNull = "Cannot post. Message data is either missing or not in the correct form.";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.isNull(), Mockito.anyInt())).thenThrow(new TwitterServiceException(errorNull));

        Response response = twitterResourceTest.postTweet(null);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorNull, response.getEntity());
    }
}

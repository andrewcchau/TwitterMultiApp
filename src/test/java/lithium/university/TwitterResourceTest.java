package lithium.university;

import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.TwitterPost;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private TwitterPost mockPost(String message) {
        TwitterPost post = Mockito.mock(TwitterPost.class);
        Mockito.when(post.getTwitterMessage()).thenReturn(message);
        return post;
    }

    private TwitterPost mockPost() {
        return mockPost("General message");
    }

    @Test
    public void testResourceGetTimeline() throws TwitterException {
        List<TwitterPost> fakeList = new ArrayList<>();
        fakeList.add(mockPost());

        Mockito.when(twitterServiceTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenReturn(Optional.of(fakeList));
        Mockito.when(twitterServiceTest.getAuthenticatedTwitter()).thenReturn(Mockito.mock(Twitter.class));

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(1, ((List<TwitterPost>) response.getEntity()).size());
        Assert.assertEquals(mockPost().getTwitterMessage(), ((List<TwitterPost>) response.getEntity()).get(0).getTwitterMessage());
    }

    @Test
    public void testResourceGetErrorHandling() throws TwitterException {
        Mockito.when(twitterServiceTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.getHomeTimeline();
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourceGetFilterSomething() throws TwitterException, TwitterServiceException {
        List<TwitterPost> fakeList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            fakeList.add(mockPost("Sample " + i));
        }

        Mockito.when(twitterServiceTest.retrieveFilteredFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt(), Mockito.any(Optional.class))).thenReturn(Optional.of(fakeList));
        Mockito.when(twitterServiceTest.getAuthenticatedTwitter()).thenReturn(Mockito.mock(Twitter.class));

        Response response = twitterResourceTest.getFilteredTweets(Optional.of("Sample"));

        List<String> list = (List<String>) ((Stream) ((Tweet) response.getEntity()).getContent().get()).collect(Collectors.toList());

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(5,  list.size());
        Assert.assertEquals(fakeList.get(0).getTwitterMessage() , list.get(0));
    }

    @Test
    public void testResourceGetFilterErrorHandling() throws TwitterException, TwitterServiceException {
        Mockito.when(twitterServiceTest.retrieveFilteredFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt(), Mockito.any(Optional.class))).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.getFilteredTweets(Optional.of("Anything"));
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourceGetFilterErrorServiceHandling() throws TwitterException, TwitterServiceException {
        String errorMessage = "Some general error message";

        Mockito.when(twitterServiceTest.retrieveFilteredFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt(), Mockito.any(Optional.class))).thenThrow(new TwitterServiceException(errorMessage));

        Response response = twitterResourceTest.getFilteredTweets(Optional.of("anything"));
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorMessage, response.getEntity());
    }

    @Test
    public void testResourcePost() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";
        Status s = Mockito.mock(Status.class);

        Mockito.when(s.getText()).thenReturn(message);
        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.any(Optional.class), Mockito.anyInt())).thenReturn(s);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.successMessage(message), response.getEntity());
    }

    @Test
    public void testResourcePostLengthError() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";
        String errorLength = "Cannot post. Message length should be between 0 and 280 characters";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.any(Optional.class), Mockito.anyInt())).thenThrow(new TwitterServiceException(errorLength));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorLength, response.getEntity());
    }

    @Test
    public void testResourcePostLengthZeroError() throws TwitterException, TwitterServiceException {
        String message = "";
        String errorZero = "Cannot post. Message length should be between 0 and 280 characters";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.any(Optional.class), Mockito.anyInt())).thenThrow(new TwitterServiceException(errorZero));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorZero, response.getEntity());
    }

    @Test
    public void testResourcePostErrorHandling() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.any(Optional.class), Mockito.anyInt())).thenThrow(new TwitterException(twitterResourceTest.getErrorMessage()));

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourcePostNullMessage() throws TwitterException, TwitterServiceException{
        String errorNull = "Cannot post. Message data is either missing or not in the correct form.";

        Mockito.when(twitterServiceTest.postToTwitter(Mockito.any(Twitter.class), Mockito.any(Optional.class), Mockito.anyInt())).thenThrow(new TwitterServiceException(errorNull));

        Response response = twitterResourceTest.postTweet(null);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        Assert.assertEquals(errorNull, response.getEntity());
    }
}

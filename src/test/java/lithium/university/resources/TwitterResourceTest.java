package lithium.university.resources;

import lithium.university.TwitterProvider;
import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.Tweet;
import lithium.university.models.PostTweetRequest;
import lithium.university.models.ReplyTweetRequest;
import lithium.university.services.TwitterService;
import org.junit.Before;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterResourceTest {
    private TwitterResource twitterResourceTest;
    private TwitterService twitterServiceTest;
    private long statusID = 12345;
    private PostTweetRequest postTweetRequestTest;
    private ReplyTweetRequest twitterUserReplyTest;

    @Before
    public void init() {
        twitterServiceTest = mock(TwitterService.class);
        TwitterProvider twitterProviderTest = mock(TwitterProvider.class);
        twitterResourceTest = new TwitterResource(twitterServiceTest);
        when(twitterProviderTest.get()).thenReturn(mock(Twitter.class));
        postTweetRequestTest = new PostTweetRequest();
        twitterUserReplyTest = new ReplyTweetRequest();
    }

    private Tweet mockPost(String message) {
        Tweet post = mock(Tweet.class);
        when(post.getTwitterMessage()).thenReturn(message);
        return post;
    }

    private Tweet mockPost() {
        return mockPost("General message");
    }

    @Test
    public void testResourceGetTimeline() throws TwitterException {
        List<Tweet> fakeList = new ArrayList<>();
        fakeList.add(mockPost());

        when(twitterServiceTest.retrieveFromTwitter(anyInt())).thenReturn(Optional.of(fakeList));

        Response response = twitterResourceTest.getHomeTimeline();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(1, ((List<Tweet>) response.getEntity()).size());
        assertEquals(mockPost().getTwitterMessage(), ((List<Tweet>) response.getEntity()).get(0).getTwitterMessage());
    }

    @Test
    public void testResourceGetErrorHandling() throws TwitterException {
        when(twitterServiceTest.retrieveFromTwitter(anyInt())).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.getHomeTimeline();
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourceGetFilterSomething() throws TwitterException, TwitterServiceException {
        List<Tweet> fakeList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fakeList.add(mockPost("Sample " + i));
        }

        when(twitterServiceTest.retrieveFilteredFromTwitter(anyInt(), any(Optional.class))).thenReturn(Optional.of(fakeList));

        Response response = twitterResourceTest.getFilteredTweets(Optional.of("Sample"));

        List<Tweet> list = ((List<Tweet>) response.getEntity());

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(5, list.size());
        assertEquals(fakeList.get(0).getTwitterMessage(), list.get(0).getTwitterMessage());
    }

    @Test
    public void testResourceGetFilterErrorHandling() throws TwitterException, TwitterServiceException {
        when(twitterServiceTest.retrieveFilteredFromTwitter(anyInt(), any(Optional.class))).thenThrow(TwitterException.class);

        Response response = twitterResourceTest.getFilteredTweets(Optional.of("Anything"));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourceGetFilterErrorServiceHandling() throws TwitterException, TwitterServiceException {
        String errorMessage = "Some general error message";

        when(twitterServiceTest.retrieveFilteredFromTwitter(anyInt(), any(Optional.class))).thenThrow(new TwitterServiceException(errorMessage));

        Response response = twitterResourceTest.getFilteredTweets(Optional.of("anything"));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorMessage, response.getEntity());
    }

    @Test
    public void testResourcePost() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";
        Status s = mock(Status.class);

        when(s.getText()).thenReturn(message);
        when(twitterServiceTest.postToTwitter(any(Optional.class))).thenReturn(Optional.of(s));

        postTweetRequestTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweet(Optional.ofNullable(postTweetRequestTest));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(twitterResourceTest.successMessage(message), response.getEntity());
    }

    @Test
    public void testResourcePostLengthError() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";
        String errorLength = "Cannot post. Message length should be between 0 and 280 characters";

        when(twitterServiceTest.postToTwitter(any(Optional.class))).thenThrow(new TwitterServiceException(errorLength));

        postTweetRequestTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweet(Optional.ofNullable(postTweetRequestTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorLength, response.getEntity());
    }

    @Test
    public void testResourcePostLengthZeroError() throws TwitterException, TwitterServiceException {
        String message = "";
        String errorZero = "Cannot post. Message length should be between 0 and 280 characters";

        when(twitterServiceTest.postToTwitter(any(Optional.class))).thenThrow(new TwitterServiceException(errorZero));

        postTweetRequestTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweet(Optional.ofNullable(postTweetRequestTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorZero, response.getEntity());
    }

    @Test
    public void testResourcePostErrorHandling() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";

        when(twitterServiceTest.postToTwitter(any(Optional.class))).thenThrow(new TwitterException(twitterResourceTest.getErrorMessage()));

        postTweetRequestTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweet(Optional.ofNullable(postTweetRequestTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourcePostNullMessage() throws TwitterException, TwitterServiceException {
        String errorNull = "Cannot post. Message data is either missing or not in the correct form.";

        when(twitterServiceTest.postToTwitter(any(Optional.class))).thenThrow(new TwitterServiceException(errorNull));

        Response response = twitterResourceTest.postTweet(Optional.ofNullable(null));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorNull, response.getEntity());
    }

    @Test
    public void testResourceReply() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";
        Status s = mock(Status.class);

        when(s.getText()).thenReturn(message);
        when(twitterServiceTest.replyToTweet(any(Optional.class))).thenReturn(Optional.of(s));

        twitterUserReplyTest.setStatusID(Optional.of(statusID));
        twitterUserReplyTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweetReply(Optional.of(twitterUserReplyTest));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(twitterResourceTest.successReply(message), response.getEntity());
    }

    @Test
    public void testResourceReplyMessageLengthError() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";
        String errorLength = "Cannot reply. Message length (including user tagging) should be between 0 and 280 characters";

        when(twitterServiceTest.replyToTweet(any(Optional.class))).thenThrow(new TwitterServiceException(errorLength));

        twitterUserReplyTest.setStatusID(Optional.of(statusID));
        twitterUserReplyTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweetReply(Optional.of(twitterUserReplyTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorLength, response.getEntity());
    }

    @Test
    public void testResourceReplyLengthZeroError() throws TwitterException, TwitterServiceException {
        String message = "";
        String errorLength = "Cannot reply. Message length (including user tagging) should be between 0 and 280 characters";

        when(twitterServiceTest.replyToTweet(any(Optional.class))).thenThrow(new TwitterServiceException(errorLength));

        twitterUserReplyTest.setStatusID(Optional.of(statusID));
        twitterUserReplyTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweetReply(Optional.of(twitterUserReplyTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorLength, response.getEntity());
    }

    @Test
    public void testResourceReplyErrorHandling() throws TwitterException, TwitterServiceException {
        String message = "This should not actually make it to Twitter!";

        when(twitterServiceTest.replyToTweet(any(Optional.class))).thenThrow(new TwitterException(twitterResourceTest.getErrorMessage()));

        twitterUserReplyTest.setStatusID(Optional.of(statusID));
        twitterUserReplyTest.setMessage(Optional.of(message));
        Response response = twitterResourceTest.postTweetReply(Optional.of(twitterUserReplyTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(twitterResourceTest.getErrorMessage(), response.getEntity());
    }

    @Test
    public void testResourceReplyNullMessage() throws TwitterException, TwitterServiceException {
        String errorNull = "Cannot reply. Check that both 'message' and 'statusID' data are present.";

        when(twitterServiceTest.replyToTweet(any(Optional.class))).thenThrow(new TwitterServiceException(errorNull));
        twitterUserReplyTest.setStatusID(Optional.of(statusID));
        Response response = twitterResourceTest.postTweetReply(Optional.of(twitterUserReplyTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorNull, response.getEntity());
    }

    @Test
    public void testResourceReplyNoStatusID() throws TwitterException, TwitterServiceException {
        String errorNull = "Cannot reply. Check that both 'message' and 'statusID' data are present.";

        when(twitterServiceTest.replyToTweet(any(Optional.class))).thenThrow(new TwitterServiceException(errorNull));

        twitterUserReplyTest.setStatusID(Optional.of((long) 0));
        twitterUserReplyTest.setMessage(Optional.of("Test Message"));
        Response response = twitterResourceTest.postTweetReply(Optional.of(twitterUserReplyTest));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals(errorNull, response.getEntity());
    }
}

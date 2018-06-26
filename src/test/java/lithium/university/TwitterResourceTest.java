package lithium.university;

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
    private TwitterRetrieve twitterRetrieveTest;
    private TwitterPublish twitterPublishTest;


    @Before
    public void init() {
        twitterRetrieveTest = Mockito.mock(TwitterRetrieve.class);
        twitterPublishTest = Mockito.mock(TwitterPublish.class);
    }

    private Status mockStatus() {
        return Mockito.mock(Status.class);
    }

    @Test
    public void testResourceGetTimeline() throws TwitterException {
        TwitterResource twitterResourceTest = new TwitterResource(twitterRetrieveTest, twitterPublishTest);
        List<Status> fakeList = new ArrayList<>();
        fakeList.add(mockStatus());

        Mockito.when(twitterRetrieveTest.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenReturn(fakeList);

        Object o = twitterResourceTest.getHomeTimeline();
        if (!(o instanceof Tweet)) {
            Assert.fail("Failed to grab info from timeline");
        }
        System.out.println(((Tweet) o).getContent());
        System.out.println("testResourceGetTimeline pass");
    }

    @Test
    public void testResourcePost() throws TwitterException {
        TwitterResource twitterResourceTest = new TwitterResource(twitterRetrieveTest, twitterPublishTest);
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterPublishTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        System.out.println("testResourcePost pass");
    }

    @Test
    public void testResourcePostLengthError() throws TwitterException {
        TwitterResource twitterResourceTest = new TwitterResource(twitterRetrieveTest, twitterPublishTest);
        String message = "This should not actually make it to Twitter!";

        Mockito.when(twitterPublishTest.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(false);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        System.out.println("testResourcePostLengthError pass");
    }
}

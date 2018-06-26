package lithium.university;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class TwitterTest {
    private static int TEST_CHAR_LIMIT = 280;

    class FakeResponseList<T> extends ArrayList<T> implements ResponseList<T> {
        public RateLimitStatus getRateLimitStatus() {
            return null;
        }
        public int getAccessLevel() {
            return 1;
        }
    }

    @Mock
    private Twitter twitterTest;

    @InjectMocks
    private TwitterRetrieve twitterRetrieveTest;

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

    private Status mockStatus() {
        return Mockito.mock(Status.class);
    }

    @Test
    public void testPublishCharLengthZero() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(0), TEST_CHAR_LIMIT);
        Assert.assertEquals(false, publishTest);
        System.out.println("testPublishCharLengthZero pass");
    }

    @Test
    public void testPublishCharLengthOver() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(TEST_CHAR_LIMIT + 1), TEST_CHAR_LIMIT);
        Assert.assertEquals(false, publishTest);
        System.out.println("testPublishCharLengthOver pass");
    }

    @Test
    public void testPublishCharLengthUnder() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(TEST_CHAR_LIMIT - 1), TEST_CHAR_LIMIT);
        Assert.assertEquals(true, publishTest);
        System.out.println("testPublishCharLengthUnder pass");
    }

    @Test
    public void testPublishCharLengthEqual() throws TwitterException {
        boolean publishTest = true;
        publishTest = twitterPublishTest.postToTwitter(twitterTest, genStringLength(TEST_CHAR_LIMIT), TEST_CHAR_LIMIT);
        Assert.assertEquals(true, publishTest);
        System.out.println("testPublishLengthEqual pass");
    }

    @Test(expected = NegativeArraySizeException.class)
    public void testRetrieveNegative() throws TwitterException {
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, -1);
        System.out.println("testRetrieveNegative pass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveNothing() throws TwitterException {
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, 0);
        System.out.println("testRetrieveNothing pass");
    }

    @Test
    public void testRetrieveSomething() throws TwitterException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus());
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, 1);
        System.out.println("Size Expected: 1, Size Actual: " + l.size());
        if(l.size() == 1){
            System.out.println("testRetrieveSomething pass");
        }
    }

    @Test
    public void testRetrieveMany() throws TwitterException {
        int size = 100;
        ResponseList<Status> fakeList = new FakeResponseList<>();
        for(int i = 0; i < 100; i++){
            fakeList.add(mockStatus());
        }
        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);
        List<Status> l = twitterRetrieveTest.retrieveFromTwitter(twitterTest, size);
        System.out.println("Size Expected: " + size + ", Size Actual: " + l.size());
        if(size == l.size()) {
            System.out.println("testRetrieveMany pass");
        }
    }

    @Test
    public void testResourceGetTimeline() throws TwitterException {
        TwitterRetrieve tr = Mockito.mock(TwitterRetrieve.class);
        TwitterResource twitterResourceTest = new TwitterResource(tr, twitterPublishTest);
        List<Status> fakeList = new ArrayList<>();
        fakeList.add(mockStatus());

        Mockito.when(tr.retrieveFromTwitter(Mockito.any(Twitter.class), Mockito.anyInt())).thenReturn(fakeList);

        Object o = twitterResourceTest.getHomeTimeline();
        if (!(o instanceof Tweet)) {
            Assert.fail("Failed to grab info from timeline");
        }
        System.out.println(((Tweet) o).getContent());
        System.out.println("testResourceGetTimeline pass");
    }

    @Test
    public void testResourcePost() throws TwitterException {
        TwitterPublish tp = Mockito.mock(TwitterPublish.class);
        TwitterResource twitterResourceTest = new TwitterResource(twitterRetrieveTest, tp);
        String message = "This should not actually make it to Twitter!";

        Mockito.when(tp.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        System.out.println("testResourcePost pass");
    }

    @Test
    public void testResourcePostLengthError() throws TwitterException {
        TwitterPublish tp = Mockito.mock(TwitterPublish.class);
        TwitterResource twitterResourceTest = new TwitterResource(twitterRetrieveTest, tp);
        String message = "This should not actually make it to Twitter!";

        Mockito.when(tp.postToTwitter(Mockito.any(Twitter.class), Mockito.anyString(), Mockito.anyInt())).thenReturn(false);

        Response response = twitterResourceTest.postTweet(message);
        Assert.assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        System.out.println("testResourcePostLengthError pass");
    }
}
package lithium.university;

import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.TwitterPost;
import lithium.university.services.TwitterService;
import org.apache.commons.lang3.StringUtils;
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
import twitter4j.User;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {
    @Mock
    private Twitter twitterTest;

    @InjectMocks
    private TwitterService twitterServiceTest;

    private String mockMessage = "General status message for testing";

    private Status mockStatus(String message){
        Status s = Mockito.mock(Status.class);
        User u = Mockito.mock(User.class);
        Mockito.when(s.getText()).thenReturn(message);
        Mockito.when(s.getUser()).thenReturn(u);
        Mockito.when(u.getName()).thenReturn("Tester");
        Mockito.when(u.getProfileImageURL()).thenReturn("http://www.url.fake");
        Mockito.when(u.getScreenName()).thenReturn("Mr. Tester");
        return s;
    }

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

    @Test (expected = TwitterServiceException.class)
    public void testPostNull() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.postToTwitter(twitterTest, Optional.ofNullable(null), TwitterApplication.TWEET_LENGTH);
        } catch(TwitterServiceException tse) {
            assertEquals("Cannot post. Message data is either missing or not in the correct form.", tse.getMessage());
            throw tse;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test (expected = TwitterServiceException.class)
    public void testPostCharLengthZero() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.postToTwitter(twitterTest, Optional.of(generateStringLength(0)), TwitterApplication.TWEET_LENGTH);
        } catch(TwitterException te){
            assertEquals("Message length must be greater than 0", te.getMessage());
            throw te;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test (expected = TwitterServiceException.class)
    public void testPostCharLengthOver() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.postToTwitter(twitterTest, Optional.of(generateStringLength(TwitterApplication.TWEET_LENGTH + 1)), TwitterApplication.TWEET_LENGTH);
        } catch(TwitterException te) {
            assertEquals("Message length should not exceed 280 characters", te.getMessage());
            throw te;
        }
        fail("TwitterException did not throw!");
    }

    @Test
    public void testPostCharLengthUnder() throws TwitterException, TwitterServiceException {
        Status publishTest = twitterServiceTest.postToTwitter(twitterTest, Optional.of(generateStringLength(TwitterApplication.TWEET_LENGTH - 1)), TwitterApplication.TWEET_LENGTH).get();
        assertNotNull(publishTest);
        assertEquals(mockMessage, publishTest.getText());
    }

    @Test
    public void testPostCharLengthEqual() throws TwitterException, TwitterServiceException {
        Status publishTest = twitterServiceTest.postToTwitter(twitterTest, Optional.of(generateStringLength(TwitterApplication.TWEET_LENGTH)), TwitterApplication.TWEET_LENGTH).get();
        assertNotNull(publishTest);
        assertEquals(mockMessage, publishTest.getText());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRetrieveNegative() throws TwitterException {
        twitterServiceTest.retrieveFromTwitter(twitterTest, -1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testRetrieveNothing() throws TwitterException {
        twitterServiceTest.retrieveFromTwitter(twitterTest, 0);
    }

    @Test
    public void testRetrieveSomething() throws TwitterException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus());

        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);

        Optional<List<TwitterPost>> l = twitterServiceTest.retrieveFromTwitter(twitterTest, 1);
        assertEquals(1, l.get().size());
        assertEquals(mockStatus().getText(), l.get().get(0).getTwitterMessage());
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

        Optional<List<TwitterPost>> l = twitterServiceTest.retrieveFromTwitter(twitterTest, size);
        assertEquals(size, l.get().size());
        for(int i = 0; i < size; i++){
            assertEquals(testMessage + i, l.get().get(i).getTwitterMessage());
        }
    }

    @Test
    public void testRetrieveFiltered() throws TwitterException, TwitterServiceException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus("Tester 1"));
        fakeList.add(mockStatus("Tester 2"));

        Mockito.when(twitterTest.getHomeTimeline(Mockito.any(Paging.class))).thenReturn(fakeList);

        Optional<List<TwitterPost>> l = twitterServiceTest.retrieveFilteredFromTwitter(twitterTest, 1, Optional.of("1"));
        assertEquals(1, l.get().size());
        assertEquals("Tester 1", l.get().get(0).getTwitterMessage());
    }

    @Test (expected = TwitterServiceException.class)
    public void testRetrieveFilterNullKeyword() throws TwitterException, TwitterServiceException {
        twitterServiceTest.retrieveFilteredFromTwitter(twitterTest, 1, Optional.ofNullable(null));
    }

    @Test
    public void testAuthenticationReturnNull() {
        twitterServiceTest.setTwitterProperties(null);
        assertNull(twitterServiceTest.getAuthenticatedTwitter());
    }

    @Test
    public void testAuthenticationReturnNonNull() {
        TwitterProperties twitterProperties = Mockito.mock(TwitterProperties.class);
        twitterServiceTest.setTwitterProperties(twitterProperties);
        assertNotNull(twitterServiceTest.getAuthenticatedTwitter());
    }
}

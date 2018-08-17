package lithium.university.services;

import lithium.university.TwitterCache;
import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.FakeResponseList;
import lithium.university.models.TwitterPost;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {
    @Mock
    private Twitter twitterTest;

    @Mock
    private TwitterCache twitterCacheTest;

    @InjectMocks
    private TwitterService twitterServiceTest;

    private String mockMessage = "General status message for testing";

    private long statusID = 12345;

    private Status mockStatus(String message) {
        Status s = mock(Status.class);
        User u = mock(User.class);
        when(s.getText()).thenReturn(message);
        when(s.getUser()).thenReturn(u);
        when(u.getName()).thenReturn("Tester");
        when(u.getProfileImageURL()).thenReturn("http://www.url.fake");
        when(u.getScreenName()).thenReturn("Mr. Tester");
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
        Status s = mock(Status.class);
        when(s.getText()).thenReturn(mockMessage);
        when(twitterTest.updateStatus(anyString())).thenReturn(s);
        when(twitterTest.updateStatus(Mockito.any(StatusUpdate.class))).thenReturn(s);
    }

    @Test(expected = TwitterServiceException.class)
    public void testPostNull() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.postToTwitter(Optional.ofNullable(null));
        } catch (TwitterServiceException tse) {
            assertEquals("Cannot post. Message data is either missing or not in the correct form.", tse.getMessage());
            throw tse;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test(expected = TwitterServiceException.class)
    public void testPostCharLengthZero() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.postToTwitter(Optional.of(generateStringLength(0)));
        } catch (TwitterServiceException tse) {
            assertEquals("Cannot post. Message length should be between 0 and 280 characters", tse.getMessage());
            throw tse;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test(expected = TwitterServiceException.class)
    public void testPostCharLengthOver() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.postToTwitter(Optional.of(generateStringLength(TwitterService.MAX_TWEET_LENGTH + 1)));
        } catch (TwitterServiceException tse) {
            assertEquals("Cannot post. Message length should be between 0 and 280 characters", tse.getMessage());
            throw tse;
        }
        fail("TwitterException did not throw!");
    }

    @Test
    public void testPostCharLengthUnder() throws TwitterException, TwitterServiceException {
        Status publishTest = twitterServiceTest.postToTwitter(Optional.of(generateStringLength(TwitterService.MAX_TWEET_LENGTH - 1))).get();
        assertNotNull(publishTest);
        assertEquals(mockMessage, publishTest.getText());
    }

    @Test
    public void testPostCharLengthEqual() throws TwitterException, TwitterServiceException {
        Status publishTest = twitterServiceTest.postToTwitter(Optional.of(generateStringLength(TwitterService.MAX_TWEET_LENGTH))).get();
        assertNotNull(publishTest);
        assertEquals(mockMessage, publishTest.getText());
    }

    @Test(expected = TwitterServiceException.class)
    public void testReplyToTweetZeroLength() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.replyToTweet(Optional.of(statusID), Optional.of(generateStringLength(0)));
        } catch (TwitterServiceException tse) {
            assertEquals("Cannot reply. Status referenced by ID does not exist.", tse.getMessage());
            throw tse;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test(expected = TwitterServiceException.class)
    public void testReplyCharLengthOver() throws TwitterException, TwitterServiceException {
        try {
            Status mockStatus = mockStatus();
            when(twitterTest.showStatus(Mockito.anyLong())).thenReturn(mockStatus);
            twitterServiceTest.replyToTweet(Optional.of(statusID), Optional.of(generateStringLength(280)));
        } catch (TwitterServiceException tse) {
            assertEquals("Cannot reply. Message length (including user tagging) should be between 0 and 280 characters", tse.getMessage());
            throw tse;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test
    public void testReplyCharLengthUnder() throws TwitterException, TwitterServiceException {
        Status mockStatus = mockStatus();
        when(twitterTest.showStatus(Mockito.anyLong())).thenReturn(mockStatus);
        Status replyTest = twitterServiceTest.replyToTweet(Optional.of(statusID),
                Optional.of(generateStringLength(2))).get();
        assertNotNull(replyTest);
        assertEquals(mockMessage, replyTest.getText());
    }

    @Test
    public void testReplyCharLengthEqual() throws TwitterException, TwitterServiceException {
        Status mockStatus = mockStatus();
        when(twitterTest.showStatus(Mockito.anyLong())).thenReturn(mockStatus);
        Status replyTest = twitterServiceTest.replyToTweet(Optional.of(statusID),
                Optional.of(generateStringLength(280 - mockStatus.getUser().getScreenName().length() - 2))).get();
        assertNotNull(replyTest);
        assertEquals(mockMessage, replyTest.getText());
    }

    @Test(expected = TwitterServiceException.class)
    public void testReplyNullStatusID() throws TwitterException, TwitterServiceException {
        try {
            twitterServiceTest.replyToTweet(Optional.ofNullable(null), Optional.of(mockMessage));
        } catch (TwitterServiceException tse) {
            assertEquals("Cannot reply. Check that both 'message' and 'statusID' data are present.", tse.getMessage());
            throw tse;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test(expected = TwitterServiceException.class)
    public void testReplyNoStatusID() throws TwitterException, TwitterServiceException {
        Long noID = (long) 0;
        try {
            twitterServiceTest.replyToTweet(Optional.ofNullable(noID), Optional.of(mockMessage));
        } catch (TwitterServiceException tse) {
            assertEquals("Cannot reply. Check that both 'message' and 'statusID' data are present.", tse.getMessage());
            throw tse;
        }
        fail("TwitterServiceException did not throw!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveNegative() throws TwitterException {
        twitterServiceTest.retrieveFromTwitter(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveNothing() throws TwitterException {
        twitterServiceTest.retrieveFromTwitter(0);
    }

    @Test
    public void testRetrieveSomething() throws TwitterException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus());

        when(twitterCacheTest.getCachedList()).thenReturn(fakeList);

        Optional<List<TwitterPost>> l = twitterServiceTest.retrieveFromTwitter(1);
        assertEquals(1, l.get().size());
        assertEquals(mockStatus().getText(), l.get().get(0).getTwitterMessage());
    }

    @Test
    public void testRetrieveMany() throws TwitterException {
        int size = 10;
        String testMessage = "Many num ";
        ResponseList<Status> fakeList = new FakeResponseList<>();
        for (int i = 0; i < size; i++) {
            fakeList.add(mockStatus(testMessage + i));
        }

        when(twitterCacheTest.getCachedList()).thenReturn(fakeList);

        Optional<List<TwitterPost>> l = twitterServiceTest.retrieveFromTwitter(size);
        assertEquals(size, l.get().size());
        for (int i = 0; i < size; i++) {
            assertEquals(testMessage + i, l.get().get(i).getTwitterMessage());
        }
    }

    @Test
    public void testRetrieveFiltered() throws TwitterException, TwitterServiceException {
        ResponseList<Status> fakeList = new FakeResponseList<>();
        fakeList.add(mockStatus("Tester 1"));
        fakeList.add(mockStatus("Tester 2"));

        when(twitterCacheTest.getCachedList()).thenReturn(fakeList);

        Optional<List<TwitterPost>> l = twitterServiceTest.retrieveFilteredFromTwitter(1, Optional.of("1"));
        assertEquals(1, l.get().size());
        assertEquals("Tester 1", l.get().get(0).getTwitterMessage());
    }

    @Test(expected = TwitterServiceException.class)
    public void testRetrieveFilterNullKeyword() throws TwitterException, TwitterServiceException {
        twitterServiceTest.retrieveFilteredFromTwitter(1, Optional.ofNullable(null));
    }
}

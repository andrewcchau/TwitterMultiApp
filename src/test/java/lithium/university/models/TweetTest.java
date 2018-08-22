package lithium.university.models;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TweetTest {
    private Tweet tweetTest = new Tweet();

    @Test
    public void testMessageGetterAndSetter() {
        String message = "Hello World";
        tweetTest.setTwitterMessage(message);
        assertEquals(message, tweetTest.getTwitterMessage());
    }

    @Test
    public void testUserGetterAndSetter() {
        TwitterUser twitterUserTest = new TwitterUser("X", "Y", "Z");
        tweetTest.setUser(twitterUserTest);
        assertEquals(twitterUserTest, tweetTest.getUser());
        assertEquals(twitterUserTest.getName(), tweetTest.getUser().getName());
        assertEquals(twitterUserTest.getProfileImageURL(), tweetTest.getUser().getProfileImageURL());
        assertEquals(twitterUserTest.getTwitterHandle(), tweetTest.getUser().getTwitterHandle());
    }

    @Test
    public void testCreatedAtGetterAndSetter() {
        Date date = new Date();
        tweetTest.setCreatedAt(date);
        assertEquals(date, tweetTest.getCreatedAt());
    }
}

package lithium.university;

import lithium.university.models.TwitterPost;
import lithium.university.models.TwitterUser;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TwitterPostTest {
    private TwitterPost twitterPostTest = new TwitterPost();

    @Test
    public void testMessageGetterAndSetter() {
        String message = "Hello World";
        twitterPostTest.setTwitterMessage(message);
        assertEquals(message, twitterPostTest.getTwitterMessage());
    }

    @Test
    public void testUserGetterAndSetter() {
        TwitterUser twitterUserTest = new TwitterUser("X", "Y", "Z");
        twitterPostTest.setUser(twitterUserTest);
        assertEquals(twitterUserTest, twitterPostTest.getUser());
        assertEquals(twitterUserTest.getName(), twitterPostTest.getUser().getName());
        assertEquals(twitterUserTest.getProfileImageURL(), twitterPostTest.getUser().getProfileImageURL());
        assertEquals(twitterUserTest.getTwitterHandle(), twitterPostTest.getUser().getTwitterHandle());
    }

    @Test
    public void testCreatedAtGetterAndSetter() {
        Date date = new Date();
        twitterPostTest.setCreatedAt(date);
        assertEquals(date, twitterPostTest.getCreatedAt());
    }
}

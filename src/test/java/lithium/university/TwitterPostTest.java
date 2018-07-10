package lithium.university;

import lithium.university.models.TwitterPost;
import lithium.university.models.TwitterUser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class TwitterPostTest {
    private TwitterPost twitterPostTest = new TwitterPost();

    @Test
    public void testMessageGetterAndSetter() {
        String message = "Hello World";
        twitterPostTest.setTwitterMessage(message);
        Assert.assertEquals(message, twitterPostTest.getTwitterMessage());
    }

    @Test
    public void testUserGetterAndSetter() {
        TwitterUser twitterUserTest = new TwitterUser("X", "Y", "Z");
        twitterPostTest.setUser(twitterUserTest);
        Assert.assertEquals(twitterUserTest, twitterPostTest.getUser());
        Assert.assertEquals(twitterUserTest.getName(), twitterPostTest.getUser().getName());
        Assert.assertEquals(twitterUserTest.getProfileImageURL(), twitterPostTest.getUser().getProfileImageURL());
        Assert.assertEquals(twitterUserTest.getTwitterHandle(), twitterPostTest.getUser().getTwitterHandle());
    }

    @Test
    public void testCreatedAtGetterAndSetter() {
        Date date = new Date();
        twitterPostTest.setCreatedAt(date);
        Assert.assertEquals(date, twitterPostTest.getCreatedAt());
    }
}

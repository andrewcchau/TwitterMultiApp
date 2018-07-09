package lithium.university;

import lithium.university.models.TwitterUser;
import org.junit.Assert;
import org.junit.Test;

public class TwitterUserTest {
    private TwitterUser twitterUserTest = new TwitterUser();

    @Test
    public void testTwitterHandleGetterAndSetter() {
        String handle = "Tester";
        twitterUserTest.setTwitterHandle(handle);
        Assert.assertEquals(handle, twitterUserTest.getTwitterHandle().get());
    }

    @Test
    public void testNameGetterAndSetter() {
        String name = "Mr. Tester";
        twitterUserTest.setName(name);
        Assert.assertEquals(name, twitterUserTest.getName().get());
    }

    @Test
    public void testImageURLGetterAndSetter() {
        String url = "http://www.url.fake";
        twitterUserTest.setProfileImageURL(url);
        Assert.assertEquals(url, twitterUserTest.getProfileImageURL().get());
    }
}

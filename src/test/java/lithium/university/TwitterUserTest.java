package lithium.university;

import lithium.university.models.TwitterUser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TwitterUserTest {
    private TwitterUser twitterUserTest = new TwitterUser();

    @Test
    public void testTwitterHandleGetterAndSetter() {
        String handle = "Tester";
        twitterUserTest.setTwitterHandle(handle);
        assertEquals(handle, twitterUserTest.getTwitterHandle());
    }

    @Test
    public void testNameGetterAndSetter() {
        String name = "Mr. Tester";
        twitterUserTest.setName(name);
        assertEquals(name, twitterUserTest.getName());
    }

    @Test
    public void testImageURLGetterAndSetter() {
        String url = "http://www.url.fake";
        twitterUserTest.setProfileImageURL(url);
        assertEquals(url, twitterUserTest.getProfileImageURL());
    }
}

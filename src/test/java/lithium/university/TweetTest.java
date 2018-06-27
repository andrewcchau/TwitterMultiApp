package lithium.university;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TweetTest {
    private Tweet tweetTest;

    @Before
    public void init(){
        tweetTest = new Tweet();
    }

    @Test
    public void testTweetGetter(){
        String test = "Hello";
        tweetTest = new Tweet(test);
        Assert.assertEquals(test, (String) tweetTest.getContent());
    }
}

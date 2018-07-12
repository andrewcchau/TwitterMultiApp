package lithium.university;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(test, tweetTest.getContent().get());
    }
}

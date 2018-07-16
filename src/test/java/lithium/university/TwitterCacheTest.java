package lithium.university;

import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterCacheTest {
    @Test
    public void testCacheList() {
        String message = "Hello World";
        ResponseList<Status> list = new FakeResponseList<>();
        Status t = mock(Status.class);
        when(t.getText()).thenReturn(message);
        list.add(t);

        TwitterCache twitterCacheTest = new TwitterCache();
        twitterCacheTest.cacheList(list, 1);
        assertNotNull(twitterCacheTest.getCachedList());
        assertEquals(list, twitterCacheTest.getCachedList());
        assertEquals(message, twitterCacheTest.getCachedList().get(0).getText());
    }
}

package lithium.university;

import org.junit.Before;
import org.junit.Test;
import twitter4j.ResponseList;
import twitter4j.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TwitterCacheTest {
    private TwitterCache twitterCacheTest;
    private ResponseList<Status> list;
    private String message = "Hello World";

    @Before
    public void init() {
        Status t = mock(Status.class);
        list = new FakeResponseList<>();
        when(t.getText()).thenReturn(message);
        list.add(t);
        twitterCacheTest = new TwitterCache();
    }

    @Test
    public void testCacheList() {
        twitterCacheTest.cacheList(list, 10);
        assertNotNull(twitterCacheTest.getCachedList());
        assertEquals(list, twitterCacheTest.getCachedList());
        assertEquals(message, twitterCacheTest.getCachedList().get(0).getText());
    }

    @Test
    public void testClearCache() {
        twitterCacheTest.cacheList(list, 10);

        assertNotNull(twitterCacheTest.getCachedList());

        twitterCacheTest.clearCache();

        assertNull(twitterCacheTest.getCachedList());
    }

    @Test
    public void testTTLClearing() {
        int ttlSeconds = 2;
        twitterCacheTest.cacheList(list, ttlSeconds);
        assertNotNull(twitterCacheTest.getCachedList());

        try {
            Thread.sleep((ttlSeconds + 1)* 1000);
        } catch (Exception e) {
            fail();
        }

        assertNull(twitterCacheTest.getCachedList());
    }
}

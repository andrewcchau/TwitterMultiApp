package lithium.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.ResponseList;
import twitter4j.Status;

import javax.inject.Inject;

public class TwitterCache {
    private static Logger logger = LoggerFactory.getLogger(TwitterCache.class);
    private ResponseList<Status> cache = null;
    private java.util.Date expiration = null;

    @Inject
    public TwitterCache() {}

    /*
     * Puts a new list into the cache list, overwriting the previous one (if it existed)
     * */
    public void cacheList(ResponseList<Status> listToCache, int secondsToLive) {
        logger.info("Caching new list that will live for " + secondsToLive + " second(s).");
        cache = listToCache;
        if (secondsToLive != 0) {
            expiration = new java.util.Date();
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.add(cal.SECOND, secondsToLive);
            expiration = cal.getTime();
        }
    }

    /*
     * Will always perform an expiration check before returning anything
     * Returns null if the cached object is expired or does not exist
     * */
    public ResponseList<Status> getCachedList() {
        if (!this.isExpired() && cache != null) {
            return cache;
        } else {
            return null;
        }
    }

    /*
     * Checks to see if cached list is expired. If it is, removes it from the cache
     * */
    private boolean isExpired() {
        if (expiration != null && expiration.before(new java.util.Date())) {
            /* Cached list has expired. Remove from memory */
            cache = null;
            return true;
        }

        return false;
    }

    public void clearCache() {
        cache = null;
    }
}

package lithium.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

public class TwitterRetrieve {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TwitterRetrieve() {
        logger.debug("Created TwitterRetrieve object");
    }

    /*
    * Gets the data from twitter and returns a list of the statuses
    * */
    public List<Status> retrieveFromTwitter(Twitter twitter, final int tweetTotal) throws TwitterException {
        Paging p = new Paging(1, tweetTotal);
        logger.trace("Attempting to grab " + tweetTotal + " tweets from Twitter timeline");
        return twitter.getHomeTimeline(p);
    }
}

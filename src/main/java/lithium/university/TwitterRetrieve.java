package lithium.university;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;

public class TwitterRetrieve {
    /*
    * Gets the data from twitter and returns a list of the statuses
    * */
    public List<Status> retrieveFromTwitter(Twitter twitter, final int tweetTotal) throws TwitterException {
        String timeline[] = new String[tweetTotal];
        Paging p = new Paging(1, tweetTotal);
        return twitter.getHomeTimeline(p);
    }
}

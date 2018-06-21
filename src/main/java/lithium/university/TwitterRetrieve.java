package lithium.university;

import twitter4j.*;
import java.util.List;

public class TwitterRetrieve {

    public static void main(String[] args) {
        try{
            TwitterRetrieve t = new TwitterRetrieve();
            Twitter twitter = TwitterFactory.getSingleton();
            t.retrieveFromTwitter(twitter, 30);
        }catch(TwitterException e){
            e.printStackTrace();
        }
    }

    /*
    * Gets the data from twitter and returns a list of the statuses
    * */
    public List<Status> retrieveFromTwitter(Twitter twitter, final int TWEET_TOTAL) throws TwitterException {
        String timeline[] = new String[TWEET_TOTAL];
        Paging p = new Paging(1, TWEET_TOTAL);
        return twitter.getHomeTimeline(p);
    }
}

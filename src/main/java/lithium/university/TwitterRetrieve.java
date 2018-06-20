package lithium.university;

import twitter4j.*;
import twitter4j.json.DataObjectFactory;
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
    * Gets the data from twitter and returns raw JSON data
    * */
    public String[] retrieveFromTwitter(Twitter twitter, final int TWEET_TOTAL) throws TwitterException {
        String timeline[] = new String[TWEET_TOTAL];
        int index = 0;
        Paging p = new Paging(1, TWEET_TOTAL);
        List<Status> tweets = twitter.getHomeTimeline(p);
        for(Status s: tweets){
            timeline[index++] = DataObjectFactory.getRawJSON(s);
        }

        return timeline;
    }

    public String retrieveLatestUserTweet(Twitter twitter) throws TwitterException {
        Paging p = new Paging(1, 1);
        List tweet_list = twitter.getUserTimeline(p);
        if(!tweet_list.isEmpty() && tweet_list.size() != 0) {
            return DataObjectFactory.getRawJSON(tweet_list.get(0));
        }
        return null;
    }
}

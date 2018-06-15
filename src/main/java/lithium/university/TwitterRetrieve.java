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


    public void retrieveFromTwitter(Twitter twitter, final int TWEET_TOTAL) throws TwitterException {
        /*Fetch a titak.. I mean number of tweets*/
        Paging p = new Paging(1, TWEET_TOTAL);
        List<Status> tweets = twitter.getHomeTimeline(p);
        for(Status s: tweets){
            System.out.println("=======================================");
            System.out.println(s.getUser().getName() + ": " + s.getText());
            System.out.println("=======================================\n\n");
        }
    }
}

import twitter4j.*;
import java.util.List;

public class TwitterRetrieve {
    public static void main(String[] args) {
        try{
            TwitterRetrieve t = new TwitterRetrieve();
            Twitter twit = TwitterFactory.getSingleton();
            t.retrieveFromTwitter(twit);
        }catch(TwitterException e){
            e.printStackTrace();
        }
    }
    public void retrieveFromTwitter(Twitter t) throws TwitterException {
        /*Fetch a titak.. I mean number of tweets*/
        int tweetTotal = 30;
        Paging p = new Paging(1, tweetTotal);
        List<Status> tweets = t.getHomeTimeline(p);
        for(Status s: tweets){
            System.out.println("=======================================");
            System.out.println(s.getUser().getName() + ": " + s.getText());
            System.out.println("=======================================\n\n");
        }
    }
}

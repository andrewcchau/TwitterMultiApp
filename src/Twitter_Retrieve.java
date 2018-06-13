import twitter4j.*;
import java.util.List;

public class Twitter_Retrieve {
    public static void main(String[] args) {
        try{
            Twitter_Retrieve t = new Twitter_Retrieve();
            Twitter twit = TwitterFactory.getSingleton();
            t.retrieveFromTwitter(twit);
        }catch(TwitterException e){
            e.printStackTrace();
        }
    }
    public void retrieveFromTwitter(Twitter twit) throws TwitterException {
        //Twitter twit = TwitterFactory.getSingleton();

        /*Fetch a titak.. I mean number of tweets*/
        int tweetTotal = 30;
        Paging p = new Paging(1, tweetTotal);
        List<Status> tweets = twit.getHomeTimeline(p);
        for(Status s: tweets){
            System.out.println("=======================================");
            System.out.println(s.getUser().getName() + ": " + s.getText());
            System.out.println("=======================================\n\n");
        }
    }
}

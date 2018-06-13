import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import java.util.Scanner;

public class TwitterPublish {
    private static final int TWEETLENGTH = 280;

    public static void main(String args[]){
        TwitterPublish tp = new TwitterPublish();
        Twitter twitter = TwitterFactory.getSingleton();
        Scanner sc = new Scanner(System.in);

        try{
            String message = sc.nextLine();
            tp.postToTwitter(twitter, message);
        }catch(TwitterException te){
            te.printStackTrace();
        }finally{
            sc.close();
        }
    }

    public void postToTwitter(Twitter twitter, String message) throws TwitterException {
        if(message.length() > TWEETLENGTH) {
            System.out.println("Cannot post. Message length should not exceed 280 characters. You're " + (message.length() - TWEETLENGTH) + " characters too long.");
            return;
        }
        Status status = twitter.updateStatus(message);
        System.out.println("Successfully updated status to: " + status.getText());
    }
}

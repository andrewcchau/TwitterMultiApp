import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.Status;
import twitter4j.TwitterFactory;
import java.util.Scanner;

public class Twitter_Publish {
    public static void main(String args[]){
        Twitter_Publish tp = new Twitter_Publish();
        Twitter twit = TwitterFactory.getSingleton();
        Scanner sc = new Scanner(System.in);

        try{
            String message = sc.nextLine();
            tp.postToTwitter(twit, message);
        }catch(TwitterException te){
            te.printStackTrace();
        }finally{
            sc.close();
        }
    }

    public void postToTwitter(Twitter twit, String message) throws TwitterException {
        if(message.length() > 280) {
            System.out.println("Cannot post. Message length should not exceed 280 characters. You're " + (message.length() - 280) + " characters too long.");
            return;
        }
        Status status = twit.updateStatus(message);
        System.out.println("Successfully updated status to: " + status.getText());
    }
}

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.Status;
import twitter4j.TwitterFactory;

public class Twitter_Publish {
    public static void main(String args[]){
        Twitter_Publish tp = new Twitter_Publish();

        try{
            String message = "Test post 2: char limit not 280 ";
            for(int i = message.length(); i < 279; i++){
                message += "i";
            }
            tp.postToTwitter(message);
        }catch(TwitterException te){
            te.printStackTrace();
        }
    }

    public void postToTwitter(String message) throws TwitterException {
        if(message.length() > 280) {
            throw new TwitterException("Error: Message length should not exceed 280 characters");
        }
        Twitter twit = TwitterFactory.getSingleton();
        Status status = twit.updateStatus(message);
        System.out.println("Successfully updated status to: " + status.getText());
    }
}

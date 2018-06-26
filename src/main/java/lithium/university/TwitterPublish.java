package lithium.university;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterPublish {
    /*
    * Takes a message and error checks it before attempting to post to user's twitter
    * Input: Twitter twitter - twitter instance, String message - message to be posted, int tweetTotal - total limited characters
    * Output: true if successful, false otherwise
    * */
    public boolean postToTwitter(Twitter twitter, String message, int tweetTotal) throws TwitterException {
        if (message.length() <= 0) {
            System.out.println("Cannot post. Message length needs to be greater than 0.");
            return false;
        }else if (message.length() > tweetTotal) {
            System.out.println("Cannot post. Message length should not exceed 280 characters. You're " + (message.length() - tweetTotal) + " characters too long.");
            return false;
        }
        Status status = twitter.updateStatus(message);
        System.out.println("Successfully updated status to: " + status.getText());
        return true;
    }
}

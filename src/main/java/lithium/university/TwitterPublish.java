package lithium.university;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.Scanner;

public class TwitterPublish {
    /*
    * Takes a message and error checks it before attempting to post to user's twitter
    * Input: Twitter twitter - twitter instance, String message - message to be posted
    * Output: true if successful, false otherwise
    * */
    public boolean postToTwitter(Twitter twitter, String message, int TWEET_LENGTH) throws TwitterException {
        if(message.length() > TWEET_LENGTH) {
            System.out.println("Cannot post. Message length should not exceed 280 characters. You're " + (message.length() - TWEET_LENGTH) + " characters too long.");
            return false;
        }
        Status status = twitter.updateStatus(message);
        System.out.println("Successfully updated status to: " + status.getText());
        return true;
    }
}

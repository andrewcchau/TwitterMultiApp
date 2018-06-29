package lithium.university;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterPublish {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TwitterPublish() {
        logger.debug("Created TwitterPublish object");
    }

    /*
    * Takes a message and error checks it before attempting to post to user's twitter
    * Input: Twitter twitter - twitter instance, String message - message to be posted, int tweetTotal - total limited characters
    * Output: true if successful, false otherwise
    * */
    public boolean postToTwitter(Twitter twitter, String message, int tweetTotal) throws TwitterException {
        if (message.length() <= 0) {
            System.out.println("Cannot post. Message length needs to be greater than 0.");
            logger.trace("Did not post tweet. Message length was 0");
            return false;
        }else if (message.length() > tweetTotal) {
            System.out.println("Cannot post. Message length should not exceed 280 characters. You're " + (message.length() - tweetTotal) + " characters too long.");
            logger.trace("Did not post tweet. Message length exceeded 280 characters");
            return false;
        }
        Status status = twitter.updateStatus(message);
        System.out.println("Successfully updated status to: " + status.getText());
        logger.debug("Successfully posted tweet \"" + message + "\" to Twitter.");
        return true;
    }
}

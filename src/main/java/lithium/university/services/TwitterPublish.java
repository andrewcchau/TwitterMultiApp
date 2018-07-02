package lithium.university.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public final class TwitterPublish {
    private static final TwitterPublish INSTANCE = new TwitterPublish();
    private final Logger logger = LoggerFactory.getLogger(TwitterPublish.class);

    private TwitterPublish() {}

    public static TwitterPublish getInstance() {
        return INSTANCE;
    }

    /*
    * Takes a message and error checks it before attempting to post to user's twitter
    * Input: Twitter twitter - twitter instance, String message - message to be posted, int tweetTotal - total limited characters
    * Output: true if successful, false otherwise
    * */
    public boolean postToTwitter(Twitter twitter, String message, int tweetTotal) throws TwitterException {
        if (message.length() <= 0) {
            logger.info("Did not post tweet. Message length was 0");
            return false;
        }else if (message.length() > tweetTotal) {
            logger.info("Did not post tweet. Message length exceeded 280 characters. Was " + message.length() + " characters long");
            return false;
        }
        Status status = twitter.updateStatus(message);
        logger.info("Successfully updated status to: \"" + status.getText() + "\"");
        return true;
    }
}

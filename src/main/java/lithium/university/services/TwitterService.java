package lithium.university.services;

import lithium.university.TwitterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

public class TwitterService {
    private static final TwitterService INSTANCE = new TwitterService();
    private final Logger logger = LoggerFactory.getLogger(TwitterService.class);
    private TwitterProperties twitterProperties;

    private TwitterService() {}

    public static TwitterService getInstance() { return INSTANCE; }

    /*
     * Takes a message and error checks it before attempting to post to user's twitter
     * Input: Twitter twitter - twitter instance, String message - message to be posted, int tweetTotal - total limited characters
     * Output: status object of updated status if successful update, null otherwise
     * */
    public Status postToTwitter(Twitter twitter, String message, int tweetTotal) throws TwitterException, TwitterServiceException {
        if (message == null) {
            throw new TwitterServiceException("Cannot post. Message data is either missing or not in the correct form.");
        }else if (message.length() <= 0) {
            throw new TwitterServiceException("Cannot post. Message length must be greater than 0");
        }else if (message.length() > tweetTotal) {
            throw new TwitterServiceException("Cannot post. Message length should not exceed 280 characters");
        }

        Status status = twitter.updateStatus(message);
        logger.info("Successfully updated status to: \"" + status.getText() + "\"");
        return status;
    }

    /*
     * Gets the data from twitter and returns a list of the statuses
     * */
    public List<Status> retrieveFromTwitter(Twitter twitter, final int tweetTotal) throws TwitterException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting to grab " + tweetTotal + " tweets from Twitter timeline");
        return twitter.getHomeTimeline(p);
    }

    public Twitter getAuthenticatedTwitter() {
        logger.debug("Re-authenticating Twitter credentials");
        if(twitterProperties == null) {
            return null;
        }
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
        cb.setOAuthConsumerKey(twitterProperties.getConsumerKey())
                .setOAuthConsumerSecret(twitterProperties.getConsumerSecret())
                .setOAuthAccessToken(twitterProperties.getAccessToken())
                .setOAuthAccessTokenSecret(twitterProperties.getAccessTokenSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        return twitterFactory.getInstance();
    }

    public void setTwitterProperties(TwitterProperties twitterProperties) {
        this.twitterProperties = twitterProperties;
    }
}

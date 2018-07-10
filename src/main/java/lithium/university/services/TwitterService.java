package lithium.university.services;

import lithium.university.TwitterProperties;
import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.TwitterPost;
import lithium.university.models.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Status postToTwitter(Twitter twitter, Optional<String> message, int tweetTotal) throws TwitterException, TwitterServiceException {
        if(message.isPresent()) {
            logger.info("Attempting to update status");
            return Stream.of(twitter.updateStatus(message.filter(s -> s.length() > 0)
                                                         .filter(s -> s.length() <= tweetTotal)
                                                         .orElseThrow(() -> new TwitterServiceException("Cannot post. Message length should be between 0 and 280 characters"))))
                        .collect(Collectors.toList()).get(0);
        }else {
            throw new TwitterServiceException("Cannot post. Message data is either missing or not in the correct form.");
        }
    }

    /*
     * Gets the data from twitter and returns a list of the statuses
     * */
    public Optional<List<TwitterPost>> retrieveFromTwitter(Twitter twitter, final int tweetTotal) throws TwitterException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting to grab " + tweetTotal + " tweets from Twitter timeline");
        return Optional.of(twitter.getHomeTimeline(p))
                                .map(List::stream)
                                .map(s -> s.map(tp -> new TwitterPost(tp.getText(),
                                                        new TwitterUser(tp.getUser().getName(), tp.getUser().getScreenName(), tp.getUser().getProfileImageURL()),
                                                        tp.getCreatedAt())).collect(Collectors.toList()));
    }

    public Optional<List<TwitterPost>> retrieveFilteredFromTwitter(Twitter twitter, final int tweetTotal, Optional<String> keyword) throws TwitterException, TwitterServiceException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting find tweets from Twitter timeline that match keyword: " + keyword.orElseThrow(() -> new TwitterServiceException("Keyword to search cannot be null.")));
        return Optional.of(twitter.getHomeTimeline(p))
                        .map(List::stream)
                        .map(s -> s.filter(st -> st.getText().contains(keyword.orElse("")))
                                    .map(st -> new TwitterPost(st.getText(),
                                                new TwitterUser(st.getUser().getName(), st.getUser().getScreenName(), st.getUser().getProfileImageURL()),
                                                st.getCreatedAt()))
                        .collect(Collectors.toList()));
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

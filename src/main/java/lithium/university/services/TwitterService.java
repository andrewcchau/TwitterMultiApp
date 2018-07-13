package lithium.university.services;

import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.TwitterPost;
import lithium.university.models.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TwitterService {
    private final Logger logger = LoggerFactory.getLogger(TwitterService.class);

    @Inject public TwitterService() {}

    /*
     * Takes a message and error checks it before attempting to post to user's twitter
     * Input: Twitter twitter - twitter instance, String message - message to be posted, int tweetTotal - total limited characters
     * Output: status object of updated status
     * */
    public Optional<Status> postToTwitter(Twitter twitter, Optional<String> message, int tweetTotal) throws TwitterException, TwitterServiceException {
        if (message.isPresent()) {
            logger.info("Attempting to update status");
            return Optional.of(twitter.updateStatus(message.filter(s -> s.length() > 0 && s.length() <= tweetTotal)
                    .orElseThrow(() -> new TwitterServiceException("Cannot post. Message length should be between 0 and 280 characters"))));
        } else {
            throw new TwitterServiceException("Cannot post. Message data is either missing or not in the correct form.");
        }
    }

    /*
     * Gets the data from twitter and returns a list of the statuses
     * */
    public Optional<List<TwitterPost>> retrieveFromTwitter(Twitter twitter, final int tweetTotal) throws TwitterException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting to grab " + tweetTotal + " tweets from Twitter timeline");

        return Optional.of(twitter.getHomeTimeline(p)
                .stream()
                .map(tp -> new TwitterPost(
                        tp.getText(),
                        new TwitterUser(
                                tp.getUser().getName(),
                                tp.getUser().getScreenName(),
                                tp.getUser().getProfileImageURL()),
                        tp.getCreatedAt()))
                .collect(Collectors.toList()));
    }

    public Optional<List<TwitterPost>> retrieveFilteredFromTwitter(Twitter twitter, final int tweetTotal, Optional<String> keyword) throws TwitterException, TwitterServiceException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting to find tweets from Twitter timeline that match keyword: " + keyword.orElseThrow(() -> new TwitterServiceException("Keyword to search cannot be null.")));
        return Optional.of(twitter.getHomeTimeline(p)
                .stream()
                .filter(s -> s.getText().contains(keyword.orElse("")))
                .map(s -> new TwitterPost(
                        s.getText(),
                        new TwitterUser(
                                s.getUser().getName(),
                                s.getUser().getScreenName(),
                                s.getUser().getProfileImageURL()),
                        s.getCreatedAt()))
                .collect(Collectors.toList()));
    }
}

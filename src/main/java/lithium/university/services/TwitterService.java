package lithium.university.services;

import lithium.university.TwitterCache;
import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.Tweet;
import lithium.university.models.PostTweetRequest;
import lithium.university.models.ReplyTweetRequest;
import lithium.university.models.TwitterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TwitterService {
    private final Logger logger = LoggerFactory.getLogger(TwitterService.class);
    public static final int MAX_TWEET_LENGTH = 280;
    private final int CACHE_TTL_SECONDS = 60;
    private Twitter twitter;
    private TwitterCache twitterCache;
    private TwitterCache userCache;

    @Inject
    public TwitterService(Twitter twitter, TwitterCache twitterCache, TwitterCache userCache) {
        this.twitter = twitter;
        this.twitterCache = twitterCache;
        this.userCache = userCache;
    }

    /*
     * Takes a message and error checks it before attempting to post to user's twitter
     * Input: Twitter twitter - twitter instance, String message - message to be posted, int tweetTotal - total limited characters
     * Output: status object of updated status
     * */
    public Optional<Status> postToTwitter(Optional<PostTweetRequest> post) throws TwitterException, TwitterServiceException {
        if(post.isPresent()) {
            Optional<String> message = post.get().getMessage();
            if (message.isPresent()) {
                logger.info("Attempting to update status");
                return Optional.of(twitter.updateStatus(message.filter(s -> s.length() > 0 && s.length() <= MAX_TWEET_LENGTH)
                        .orElseThrow(() -> new TwitterServiceException("Cannot post. Message length should be between 0 and 280 characters"))))
                        .map(s -> {
                            twitterCache.clearCache();
                            userCache.clearCache();
                            return s;
                        });
            } else {
                throw new TwitterServiceException("Cannot post. Message data is either missing or not in the correct form.");
            }
        } else {
            throw new TwitterServiceException("Cannot post. Post data is missing.");
        }
    }

    /*
     * Takes in a tweet id and message and replies to the original tweet
     * */
    public Optional<Status> replyToTweet(Optional<ReplyTweetRequest> reply) throws TwitterException, TwitterServiceException {
        if(reply.isPresent()) {
            Optional<Long> statusID = reply.get().getStatusID();
            Optional<String> message = reply.get().getMessage();

            if (statusID.isPresent() && statusID.get() != 0 && message.isPresent()) {
                logger.info("Attempting to reply to tweet");
                Status status = twitter.showStatus(statusID.get());
                if(status != null) {
                    return Optional.of(twitter.updateStatus((new StatusUpdate("@" + status.getUser().getScreenName() + " " +
                            message.filter(s -> s.length() > 0 && s.length() <= (MAX_TWEET_LENGTH - status.getUser().getScreenName().length() - 2))
                                    .orElseThrow(() -> new TwitterServiceException("Cannot reply. Message length (including user tagging) should be between 0 and 280 characters")))
                            .inReplyToStatusId(statusID.map(s -> {
                                twitterCache.clearCache();
                                userCache.clearCache();
                                return s;
                            }).get()))));
                } else {
                    throw new TwitterServiceException("Cannot reply. Status referenced by ID does not exist.");
                }
            } else {
                throw new TwitterServiceException("Cannot reply. Check that both 'message' and 'statusID' data are present.");
            }
        } else {
            throw new TwitterServiceException("Cannot reply. Reply data is missing.");
        }
    }

    /*
     * Gets the data from twitter and returns a list of the statuses
     * */
    public Optional<List<Tweet>> retrieveFromTwitter(final int tweetTotal) throws TwitterException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting to grab " + tweetTotal + " tweets from Twitter timeline");

        ResponseList<Status> cache = twitterCache.getCachedList();

        if (cache == null) {
            twitterCache.cacheList(twitter.getHomeTimeline(p), CACHE_TTL_SECONDS);
            cache = twitterCache.getCachedList();
        }

        return Optional.of(cache
                .stream()
                .map(tp -> new Tweet(
                        tp.getText(),
                        new TwitterUser(
                                tp.getUser().getScreenName(),
                                tp.getUser().getName(),
                                tp.getUser().getProfileImageURL()),
                        tp.getCreatedAt(),
                        Long.toString(tp.getId())))
                .collect(Collectors.toList()));
    }

    public Optional<List<Tweet>> retrieveFilteredFromTwitter(final int tweetTotal, Optional<String> keyword) throws TwitterException, TwitterServiceException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting to find tweets from Twitter timeline that match keyword: " + keyword.orElseThrow(() -> new TwitterServiceException("Keyword to search cannot be null.")));

        ResponseList<Status> cache = twitterCache.getCachedList();

        if (cache == null) {
            twitterCache.cacheList(twitter.getHomeTimeline(p), CACHE_TTL_SECONDS);
            cache = twitterCache.getCachedList();
        }

        return Optional.of(cache
                .stream()
                .filter(s -> s.getText().contains(keyword.orElse("")))
                .map(s -> new Tweet(
                        s.getText(),
                        new TwitterUser(
                                s.getUser().getScreenName(),
                                s.getUser().getName(),
                                s.getUser().getProfileImageURL()),
                        s.getCreatedAt(),
                        Long.toString(s.getId())))
                .collect(Collectors.toList()));
    }

    public Optional<List<Tweet>> retrieveUserPosts(final int tweetTotal) throws TwitterException {
        Paging p = new Paging(1, tweetTotal);
        logger.debug("Attempting to grab " + tweetTotal + " tweets from User timeline");

        ResponseList<Status> cache = userCache.getCachedList();

        if (cache == null) {
            userCache.cacheList(twitter.getUserTimeline(twitter.getId(), p), CACHE_TTL_SECONDS);
            cache = userCache.getCachedList();
        }

        return Optional.of(cache
                .stream()
                .map(tp -> new Tweet(
                        tp.getText(),
                        new TwitterUser(
                                tp.getUser().getScreenName(),
                                tp.getUser().getName(),
                                tp.getUser().getProfileImageURL()),
                        tp.getCreatedAt(),
                        Long.toString(tp.getId())))
                .collect(Collectors.toList()));
    }
}

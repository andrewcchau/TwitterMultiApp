package lithium.university.resources;

import lithium.university.TwitterApplication;
import lithium.university.exceptions.TwitterServiceException;
import lithium.university.services.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;


@Path("/api/1.0/twitter")
public class TwitterResource {
    private final Logger logger = LoggerFactory.getLogger(TwitterResource.class);
    private final String errorMessage = "Oops! Something went wrong! Please check the server log for details and try again.";

    private TwitterService twitterService;

    @Inject
    public TwitterResource(TwitterService twitterService) {
        this.twitterService = twitterService;
    }


    public String successMessage(String message) {
        return "Successfully updated status to: " + message + "\n";
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    @GET
    @Path("/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeTimeline() {
        try {
            return twitterService.retrieveFromTwitter(TwitterApplication.TWEET_TOTAL).map(l -> Response.ok(l).header("Access-Control-Allow-Origin", "http://localhost:9000").build()).get();
        } catch (TwitterException te) {
            logger.error("An exception has occurred in getHomeTimeline", te);
            return Response.serverError().entity(errorMessage).header("Access-Control-Allow-Origin", "http://localhost:9000").build();
        }
    }

    @GET
    @Path("/usertimeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTimeline() {
        try {
            return twitterService.retrieveUserPosts(TwitterApplication.TWEET_TOTAL).map(l -> Response.ok(l).header("Access-Control-Allow-Origin", "http://localhost:9000").build()).get();
        } catch (TwitterException te) {
            logger.error("An exception has occurred in getUserTimeline", te);
            return Response.serverError().entity(errorMessage).header("Access-Control-Allow-Origin", "http://localhost:9000").build();
        }
    }

    @GET
    @Path("/tweet/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilteredTweets(@QueryParam("keyword") Optional<String> keyword) {
        try {
            return twitterService.retrieveFilteredFromTwitter(TwitterApplication.TWEET_TOTAL, keyword)
                    .map(l -> Response.ok(l).header("Access-Control-Allow-Origin", "http://localhost:9000").build()).get();
        } catch (TwitterException te) {
            logger.error("An exception from Twitter has occurred in getFilteredTweets", te);
            return Response.serverError().entity(errorMessage).build();
        } catch (TwitterServiceException tse) {
            logger.error("An exception from TwitterService has occurred in getFilteredTweets", tse);
            return Response.serverError().entity(tse.getMessage()).build();
        }
    }

    @POST
    @Path("/tweet")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postTweet(@FormParam("message") String message) {
        /*Attempt to post to Twitter*/
        try {
            return twitterService.postToTwitter(Optional.ofNullable(message), TwitterApplication.TWEET_LENGTH)
                    .map(status -> successMessage(status.getText()))
                    .map(status -> Response.ok(status).build())
                    .get();
        } catch (TwitterException te) {
            logger.error("An exception from Twitter has occurred in postTweet", te);
            return Response.serverError().entity(errorMessage).build();
        } catch (TwitterServiceException tse) {
            logger.error("An exception from TwitterService has occurred in postTweet", tse);
            return Response.serverError().entity(tse.getMessage()).build();
        }
    }
}
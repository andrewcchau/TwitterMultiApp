package lithium.university.resources;

import lithium.university.Tweet;
import lithium.university.TwitterApplication;
import lithium.university.TwitterProperties;
import lithium.university.exceptions.TwitterServiceException;
import lithium.university.models.TwitterPost;
import lithium.university.services.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Path("/api/1.0/twitter")
public class TwitterResource {
    private final Logger logger = LoggerFactory.getLogger(TwitterResource.class);
    private final String errorMessage = "Oops! Something went wrong! Please check the server log for details and try again.";

    private Twitter twitter;
    private TwitterService twitterService;

    public TwitterResource(){
        twitterService = TwitterService.getInstance();
    }

    public TwitterResource(TwitterProperties twitterProperties){
        this();
        twitterService.setTwitterProperties(twitterProperties);
    }

    /*
    * Used mainly for testing purposes
    * */
    public TwitterResource(TwitterService twitterService) {
        this();
        this.twitterService = twitterService;
    }

    public String successMessage(Optional<String> message){
        return "Successfully updated status to: " + message.orElse("<No message passed in>") + "\n";
    }

    public String getErrorMessage(){ return errorMessage;}


    @GET
    @Path("/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeTimeline() {
        this.getTwitterAuthentication();

        Optional<List<TwitterPost>> list;
        try {
            list = twitterService.retrieveFromTwitter(twitter, TwitterApplication.TWEET_TOTAL);
        } catch (TwitterException te) {
            logger.error("An exception has occurred in getHomeTimeline", te);
            return Response.serverError().entity(errorMessage).build();
        }

        logger.info("Successfully grabbed tweets from home timeline.");
        return Response.ok(new Tweet(list.get())).build();
    }

    @GET
    @Path("/tweet/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilteredTweets(@QueryParam("keyword") Optional<String> keyword) {
        this.getTwitterAuthentication();

        try {
            return Response.ok(new Tweet(twitterService.retrieveFilteredFromTwitter(twitter, TwitterApplication.TWEET_TOTAL, keyword)
                                                 .map(List::stream)
                                                 .map(s -> s.map(TwitterPost::getTwitterMessage).collect(Collectors.toList())))).build();
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
    public Response postTweet(@FormParam("message") String message){
        this.getTwitterAuthentication();

        /*Attempt to post to Twitter*/
        try{
             return Response.status(Response.Status.OK)
                            .entity(successMessage(Optional.ofNullable(twitterService.postToTwitter(twitter, Optional.ofNullable(message), TwitterApplication.TWEET_LENGTH).getText())))
                            .build();
        }catch(TwitterException te){
            logger.error("An exception from Twitter has occurred in postTweet", te);
            return Response.serverError().entity(errorMessage).build();
        }catch(TwitterServiceException tse) {
            logger.error("An exception from TwitterService has occurred in postTweet", tse);
            return Response.serverError().entity(tse.getMessage()).build();
        }
    }


    /*
     * Used to re-check authentication credentials
     * */
    private void getTwitterAuthentication(){
        twitter = twitterService.getAuthenticatedTwitter();
    }
}
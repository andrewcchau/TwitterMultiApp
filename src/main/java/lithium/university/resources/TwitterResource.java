package lithium.university.resources;

import lithium.university.Tweet;
import lithium.university.TwitterApplication;
import lithium.university.TwitterProperties;
import lithium.university.services.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


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

    public String successMessage(String message){
        return "Successfully updated status to: " + message + "\n";
    }

    public String getErrorMessage(){ return errorMessage;}

    public String getMessageFormError(){ return "Cannot post. Message data is either missing or not in the correct form.";}

    @GET
    @Path("/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeTimeline() {
        this.getTwitterAuthentication();

        List<Status> list = null;
        try {
            list = twitterService.retrieveFromTwitter(twitter, TwitterApplication.TWEET_TOTAL);
        } catch (TwitterException te) {
            logger.error("An exception has occurred in getHomeTimeline", te);
            return Response.serverError().entity(errorMessage).build();
        }

        logger.info("Successfully grabbed tweets from home timeline.");
        return Response.ok(new Tweet(list), MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Path("/tweet")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postTweet(@FormParam("message") String message){
        this.getTwitterAuthentication();

        /*Initial checking of message for correct form*/
        if(message == null){
            logger.debug("User passed in null message form data");
            return Response.serverError().entity(getMessageFormError()).build();
        }

        /*Attempt to post to Twitter and get error codes*/
        try{
            twitterService.postToTwitter(twitter, message, TwitterApplication.TWEET_LENGTH);
        }catch(TwitterException te){
            logger.error("An exception has occurred in postTweet", te);
            return Response.serverError().entity(te.getMessage()).build();
        }

        return Response.status(Response.Status.OK).entity(successMessage(message)).build();
    }

    /*
     * Used to re-check authentication credentials
     * */
    private void getTwitterAuthentication(){
        twitter = twitterService.getAuthenticatedTwitter();
    }
}
package lithium.university.resources;

import lithium.university.Tweet;
import lithium.university.TwitterApplication;
import lithium.university.TwitterProperties;
import lithium.university.services.TwitterPublish;
import lithium.university.services.TwitterRetrieve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

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
    private TwitterRetrieve twitterRetrieve;
    private TwitterPublish twitterPublish;
    private TwitterProperties twitterProperties;

    public TwitterResource(){
        twitterRetrieve = TwitterRetrieve.getInstance();
        twitterPublish = TwitterPublish.getInstance();
        twitterProperties = new TwitterProperties();
    }

    public TwitterResource(TwitterProperties twitterProperties){
        this();
        this.twitterProperties = twitterProperties;
    }

    /**
     * Used mainly for testing purposes
     */
    public TwitterResource(TwitterRetrieve tr, TwitterPublish tp){
        this();
        twitterRetrieve = tr;
        twitterPublish = tp;
    }

    public String errorLengthMessage(){ return "Cannot post. Message length should not exceed " + TwitterApplication.TWEET_LENGTH + " characters."; }

    public String errorZeroMessage(){
        return "Cannot post. Message length needs to be greater than 0.";
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
            list = twitterRetrieve.retrieveFromTwitter(twitter, TwitterApplication.TWEET_TOTAL);
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
        boolean post_success = false;
        boolean error = false;
        try{
            post_success = twitterPublish.postToTwitter(twitter, message, TwitterApplication.TWEET_LENGTH);
        }catch(TwitterException te){
            logger.error("An exception has occurred in postTweet", te);
            error = true;
        }

        /*Handle errors as needed*/
        if(post_success){
            return Response.status(Response.Status.OK).entity(successMessage(message)).build();
        }else if(error){
            return Response.serverError().entity(errorMessage).build();
        }

        if(message.length() > 0) {
            return Response.serverError().entity(errorLengthMessage()).build();
        }else {
            return Response.serverError().entity(errorZeroMessage()).build();
        }
    }

    /*
     * Used to re-check authentication credentials
     * */
    private void getTwitterAuthentication(){
        logger.debug("Re-authenticating Twitter credentials");
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
        cb.setOAuthConsumerKey(twitterProperties.getConsumerKey())
                .setOAuthConsumerSecret(twitterProperties.getConsumerSecret())
                .setOAuthAccessToken(twitterProperties.getAccessToken())
                .setOAuthAccessTokenSecret(twitterProperties.getAccessTokenSecret());
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }
}
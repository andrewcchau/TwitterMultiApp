package lithium.university;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/api/1.0/twitter")
public class TwitterResource {
    private final String ERROR_MESSAGE = "Oops! Something went wrong! Please check the server log for details and try again.";

    private Twitter twitter;
    private TwitterRetrieve twitterRetrieve;
    private TwitterPublish twitterPublish;


    public TwitterResource(){
        twitterRetrieve = new TwitterRetrieve();
        twitterPublish = new TwitterPublish();
    }

    public TwitterResource(TwitterRetrieve tr, TwitterPublish tp){
        twitterRetrieve = tr;
        twitterPublish = tp;
    }

    protected String errorLengthMessage(){
        return "Cannot post. Message length should not exceed " + TwitterApplication.TWEET_LENGTH + " characters.";
    }

    protected String successMessage(String message){
        return "Successfully updated status to: " + message + "\n";
    }


    @GET
    @Path("/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeTimeline() {
        this.getTwitterAuthentication();

        List<Status> list = null;
        try {
            list = twitterRetrieve.retrieveFromTwitter(twitter, TwitterApplication.TWEET_TOTAL);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(ERROR_MESSAGE).build();
        }

        return Response.ok(new Tweet(list), MediaType.APPLICATION_JSON).build();
    }


    @POST
    @Path("/tweet")
    public Response postTweet(String message){
        this.getTwitterAuthentication();

        /*Attempt to post to Twitter and get error codes*/
        boolean post_success = false;
        boolean error = false;
        try{
            post_success = twitterPublish.postToTwitter(twitter, message, TwitterApplication.TWEET_LENGTH);
        }catch(Exception e){
            e.printStackTrace();
            error = true;
        }

        /*Handle errors as needed*/
        if(post_success){
            return Response.status(Response.Status.OK).entity(successMessage(message)).build();
        }else if(error){
            return Response.serverError().entity(ERROR_MESSAGE).build();
        }

        return Response.serverError().entity(errorLengthMessage()).build();
    }

    /*
     * Used to re-check authentication credentials
     * */
    private void getTwitterAuthentication(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        twitter = twitterFactory.getInstance();
    }
}
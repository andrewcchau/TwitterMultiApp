package lithium.university;

import twitter4j.*;
import twitter4j.Twitter;
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
    private final int TWEET_TOTAL;
    private final int TWEET_LENGTH;
    private Twitter twitter;
    private TwitterRetrieve twitterRetrieve;
    private TwitterPublish twitterPublish;

    public TwitterResource(int TWEET_TOTAL, int TWEET_LENGTH){
        this.TWEET_TOTAL = TWEET_TOTAL;
        this.TWEET_LENGTH = TWEET_LENGTH;
        twitterRetrieve = new TwitterRetrieve();
        twitterPublish = new TwitterPublish();
    }


    @GET
    @Path("/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getHomeTimeline() {
        this.getTwitterAuthentication();

        List<Status> list = null;
        try {
            list = twitterRetrieve.retrieveFromTwitter(twitter, TWEET_TOTAL, TWEET_LENGTH);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }

        return new Tweet(list);
    }


    @POST
    @Path("/tweet")
    public Response postTweet(String message){
        this.getTwitterAuthentication();

        /*Attempt to post to Twitter and get error codes*/
        boolean post_success = false;
        String error_message = "";
        try{
            post_success = twitterPublish.postToTwitter(twitter, message);
        }catch(Exception e){
            e.printStackTrace();
            error_message = e.toString();
        }

        /*Handle errors as needed*/
        if(post_success){
            return Response.status(Response.Status.OK).entity("Successfully updated status to: " + message + "\n").build();
        }else if(error_message.length() > 0){
            return Response.serverError().entity("Cannot post. " + error_message).build();
        }

        return Response.serverError().entity("Cannot post. Message length should not exceed " + TWEET_LENGTH + " characters.").build();
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
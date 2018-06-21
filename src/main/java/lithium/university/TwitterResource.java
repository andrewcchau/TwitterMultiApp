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

    public TwitterResource(int TWEET_TOTAL){
        this.TWEET_TOTAL = TWEET_TOTAL;
    }

    @GET
    @Path("/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Tweet getHomeTimeline() {
        Twitter twitter = this.getTwitterAuthentication();
        TwitterRetrieve tr = new TwitterRetrieve();

        List<Status> list = null;
        try {
            list = tr.retrieveFromTwitter(twitter, TWEET_TOTAL);
        } catch (Exception e) {
            e.printStackTrace();
            return new Tweet(e.getMessage());
        }

        return new Tweet(list);
    }

    @POST
    @Path("/tweet")
    public Response postTweet(String message){
        Twitter twitter = this.getTwitterAuthentication();

        /*Attempt to post to Twitter and get error codes*/
        TwitterPublish tp = new TwitterPublish();
        boolean post_success = false;
        String error_message = "";
        try{
            post_success = tp.postToTwitter(twitter, message);
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

        return Response.serverError().entity("Cannot post. Message length should not exceed 280 characters.").build();
    }

    /*
     * Used to re-check authentication credentials
     * */
    private Twitter getTwitterAuthentication(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        return twitterFactory.getInstance();
    }
}
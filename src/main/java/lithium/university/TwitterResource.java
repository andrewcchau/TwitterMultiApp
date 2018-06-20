package lithium.university;

import lithium.university.Tweet;
import com.codahale.metrics.annotation.Timed;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.concurrent.atomic.AtomicLong;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.net.URL;
import java.net.URI;


@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class TwitterResource {
    private final AtomicLong counter;
    private final Twitter twitter;
    private final int TWEET_TOTAL;
    private final TwitterHealthCheck thc;

    public TwitterResource(Twitter twitter, int TWEET_TOTAL, TwitterHealthCheck thc){
        this.twitter = twitter;
        this.TWEET_TOTAL = TWEET_TOTAL;
        this.counter = new AtomicLong();
        this.thc = thc;
    }

    @GET
    @Path("/timeline")
    @Timed
    public Response getHomeTimeline() {
        /*Health Check*/
        if (!thc.isHealthy()) {
            return Response.status(500).type("text/plain").entity("Twitter Authentication Failure").build();
        }

        TwitterRetrieve tr = new TwitterRetrieve();
        String[] value = null;
        JSONObject[] tweet_holder = new JSONObject[TWEET_TOTAL];
        try {
            value = tr.retrieveFromTwitter(twitter, TWEET_TOTAL);
            JSONParser parser = new JSONParser();
            for (int i = 0; i < TWEET_TOTAL; i++) {
                if (value[i] != null) {
                    tweet_holder[i] = (JSONObject) parser.parse(value[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok(new Tweet(counter.incrementAndGet(), tweet_holder)).build();
    }

    @POST
    @Path("/tweet")
    public Response postTweet(String message){
        /*Health Check*/
        if (!thc.isHealthy()) {
            return Response.status(500).type("text/plain").entity("Twitter Authentication Failure").build();
        }

        TwitterPublish tp = new TwitterPublish();
        int error_code = -1;
        try{
            error_code = tp.postToTwitter(twitter, message);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(error_code != -1){
            return Response.status(Response.Status.OK).entity("Successfully updated status to: " + message + "\n").build();
        }

        return Response.serverError().entity("Cannot post. Message length should not exceed 280 characters.").build();
    }

    @GET
    @Path("/tweet")
    public Tweet getLatestUserTweet(){
        TwitterRetrieve tr = new TwitterRetrieve();
        JSONObject[] tweet_holder = new JSONObject[1];
        try {
            JSONParser parser = new JSONParser();
            tweet_holder[0] = (JSONObject) parser.parse(tr.retrieveLatestUserTweet(twitter));
        }catch(Exception e){
            e.printStackTrace();
        }

        return new Tweet(counter.incrementAndGet(), tweet_holder);
    }
}
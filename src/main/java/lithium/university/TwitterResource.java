package lithium.university;

import lithium.university.Tweet;
import com.codahale.metrics.annotation.Timed;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;


@Path("/api/1.0/twitter")
@Produces(MediaType.APPLICATION_JSON)
public class TwitterResource {
    private final String template;
    private final AtomicLong counter;
    private final Twitter twitter;
    private final int TWEET_TOTAL;

    public TwitterResource(String template, Twitter twitter, int TWEET_TOTAL){
        this.template = template;
        this.twitter = twitter;
        this.TWEET_TOTAL = TWEET_TOTAL;
        this.counter = new AtomicLong();
    }

    @GET
    @Path("/timeline")
    @Timed
    public Tweet getHomeTimeline(){
        TwitterRetrieve tr = new TwitterRetrieve();
        String[] value = null;
        JSONObject[] tweet_holder = new JSONObject[TWEET_TOTAL];
        try{
            value = tr.retrieveFromTwitter(twitter, TWEET_TOTAL);
            JSONParser parser = new JSONParser();
            for(int i = 0; i < TWEET_TOTAL; i++){
                tweet_holder[i] = (JSONObject) parser.parse(value[i]);
            }
        }catch(TwitterException te){
            te.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        return new Tweet(counter.incrementAndGet(), tweet_holder);
    }

    @POST
    @Path("/tweet")
    public Response postTweet(String message){
        TwitterPublish tp = new TwitterPublish();
        int error_code = -1;
        try{
            error_code = tp.postToTwitter(twitter, message);
        }catch(TwitterException te){
            te.printStackTrace();
        }
        if(error_code != -1){
            return Response.status(Response.Status.OK).build();
        }
        return Response.serverError().entity("{}").build();
    }

    @GET
    @Path("/tweet")
    public Tweet getLatestUserTweet(){
        TwitterRetrieve tr = new TwitterRetrieve();
        JSONObject[] tweet_holder = new JSONObject[1];
        try{
            JSONParser parser = new JSONParser();
            tweet_holder[0] = (JSONObject) parser.parse(tr.retrieveLatestUserTweet(twitter));
        }catch(TwitterException te){
            te.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        return new Tweet(counter.incrementAndGet(), tweet_holder);
    }
}
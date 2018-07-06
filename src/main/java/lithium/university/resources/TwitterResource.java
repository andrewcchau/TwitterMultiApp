package lithium.university.resources;

import lithium.university.Tweet;
import lithium.university.TwitterApplication;
import lithium.university.TwitterProperties;
import lithium.university.models.TwitterPost;
import lithium.university.services.TwitterService;
import lithium.university.services.TwitterServiceException;
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

    public String successMessage(String message){
        return "Successfully updated status to: " + message + "\n";
    }

    public String getErrorMessage(){ return errorMessage;}


    @GET
    @Path("/timeline")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeTimeline() {
        this.getTwitterAuthentication();

        List<TwitterPost> list;
        try {
            list = twitterService.retrieveFromTwitter(twitter, TwitterApplication.TWEET_TOTAL);
        } catch (TwitterException te) {
            logger.error("An exception has occurred in getHomeTimeline", te);
            return Response.serverError().entity(errorMessage).build();
        }

        logger.info("Successfully grabbed tweets from home timeline.");
        return Response.ok(new Tweet(list), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/tweet/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilteredTweets(@QueryParam("keyword") Optional<String> keyword) {
        List<TwitterPost> rawPosts = (List<TwitterPost>) ((Tweet) getHomeTimeline().getEntity()).getContent();

        List<String> filteredPosts = rawPosts.parallelStream().filter(p -> p.getTwitterMessage().contains(keyword.orElse(""))).map(TwitterPost::getTwitterMessage).collect(Collectors.toList());
        if(!filteredPosts.isEmpty()) {
            logger.info("Successfully grabbed " + filteredPosts.size() + " tweets that matched keyword");
            return Response.ok(new Tweet(filteredPosts), MediaType.APPLICATION_JSON).build();
        } else {
            logger.info("There were no tweets that contained given keyword");
            return Response.ok().entity("There are no messages within the last " + TwitterApplication.TWEET_TOTAL + " that have the keyword \"" + keyword.orElse("") + "\" in them.").build();
        }
    }

    @POST
    @Path("/tweet")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response postTweet(@FormParam("message") String message){
        this.getTwitterAuthentication();

        /*Attempt to post to Twitter*/
        Status status;
        try{
             status = twitterService.postToTwitter(twitter, message, TwitterApplication.TWEET_LENGTH);
        }catch(TwitterException te){
            logger.error("An exception from Twitter has occurred in postTweet", te);
            return Response.serverError().entity(errorMessage).build();
        }catch(TwitterServiceException tse) {
            logger.error("An exception from TwitterService has occured in postTweet", tse);
            return Response.serverError().entity(tse.getMessage()).build();
        }

        return Response.status(Response.Status.OK).entity(successMessage(status.getText())).build();
    }


    /*
     * Used to re-check authentication credentials
     * */
    private void getTwitterAuthentication(){
        twitter = twitterService.getAuthenticatedTwitter();
    }
}
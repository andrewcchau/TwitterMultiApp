import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.Scanner;

public class TwitterMain {
    private static final String P = "p";
    private static final String R = "r";
    private static final String E = "exit";
    private static final int TWEETTOTAL = 30;

    public static void main(String[] args) {
        System.out.println("Please ensure you have filled in the appropriate keys before posting/retrieving.");
        Scanner s = new Scanner(System.in);
        String holder = null;
        TwitterPublish tp = new TwitterPublish();
        TwitterRetrieve tr = new TwitterRetrieve();
        Twitter twitter = TwitterFactory.getSingleton();
        boolean loop = true;

        while(loop) {
            System.out.println("Post to timeline or retrieve from timeline? (\"p\" to post) (\"r\" to retrieve) (\"exit\" to exit)");
            holder = s.nextLine();
            try{
                if (holder.toLowerCase().equals(P)) {
                    System.out.print("Enter post: ");
                    tp.postToTwitter(twitter, s.nextLine());
                } else if (holder.toLowerCase().equals(R)) {
                    System.out.println("Here are the top " + TWEETTOTAL + " latest posts:");
                    tr.retrieveFromTwitter(twitter, TWEETTOTAL);
                } else if(holder.toLowerCase().equals(E)){
                    System.out.println("Goodbye");
                    loop = false;
                } else {
                    System.out.println("Incorrect input. Please enter \"p\", \"r\", or \"exit\"");
                }
            }catch(TwitterException te){
                te.printStackTrace();
            }
        }
        s.close();
    }
}

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.Scanner;

public class Twitter_Main {
    public static void main(String[] args) {
        System.out.println("Please ensure you have filled in the appropriate keys before posting/retrieving.");
        Scanner s = new Scanner(System.in);
        String holder = null;
        Twitter_Publish tp = new Twitter_Publish();
        Twitter_Retrieve tr = new Twitter_Retrieve();
        Twitter t = TwitterFactory.getSingleton();
        boolean loop = true;

        while(loop) {
            System.out.println("Post to timeline or retrieve from timeline? (p/r) (\"exit\" to exit)");
            holder = s.nextLine();
            try{
                if (holder.toLowerCase().equals("p")) {
                    System.out.print("Enter post: ");
                    tp.postToTwitter(t, s.nextLine());
                } else if (holder.toLowerCase().equals("r")) {
                    System.out.println("Here are the top 30 latest posts:");
                    tr.retrieveFromTwitter(t);
                } else if(holder.toLowerCase().equals("exit")){
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

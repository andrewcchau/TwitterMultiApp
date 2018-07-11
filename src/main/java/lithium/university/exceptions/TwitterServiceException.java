package lithium.university.exceptions;

public class TwitterServiceException extends RuntimeException {
    public TwitterServiceException(String message) {
        super(message);
    }

    /*Needed for the 'hacky' way of returning optional.map()... in TwitterService*/
//    public TwitterServiceException(Exception e) { super(e); }
}

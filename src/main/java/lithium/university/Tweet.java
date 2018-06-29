package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Tweet<T> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object content;

    public Tweet(){
        logger.info("Created Tweet object");
    }

    public Tweet(Object content){
        this();
        this.content = content;
    }

    @JsonProperty
    public Object getContent(){
        return content;
    }
}
package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import twitter4j.JSONObject;


public class Tweet<T> {
    private Object content;

    public Tweet(){}

    public Tweet(Object content){
        this.content = content;
    }

    @JsonProperty
    public Object getContent(){
        return content;
    }
}
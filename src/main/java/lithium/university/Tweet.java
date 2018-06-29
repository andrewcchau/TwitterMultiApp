package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Tweet<T> {
    private Object content;

    public Tweet(){ }

    public Tweet(Object content){
        this();
        this.content = content;
    }

    @JsonProperty
    public Object getContent(){
        return content;
    }
}
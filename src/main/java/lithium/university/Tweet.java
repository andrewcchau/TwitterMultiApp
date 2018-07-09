package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;


public class Tweet<T> {
    private Object content;

    public Tweet(){ }

    public Tweet(Object content){
        this();
        this.content = content;
    }

    @JsonProperty
    public Optional<Object> getContent(){
        return Optional.ofNullable(content);
    }
}
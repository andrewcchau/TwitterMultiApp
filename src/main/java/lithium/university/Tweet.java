package lithium.university;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.json.simple.JSONObject;


public class Tweet {
    private long id;

//    @Length(max = 280)
    private JSONObject[] content;

    public Tweet(){

    }

    public Tweet(long id, JSONObject[] content){
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId(){
        return id;
    }

    @JsonProperty
    public JSONObject[] getContent(){
        return content;
    }
}
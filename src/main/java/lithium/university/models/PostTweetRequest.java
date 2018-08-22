package lithium.university.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class PostTweetRequest {
    private Optional<String> message;

    @JsonProperty("message")
    public Optional<String> getMessage() {
        return message;
    }

    public void setMessage(Optional<String> message) {
        this.message = message;
    }
}

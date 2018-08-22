package lithium.university.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class ReplyTweetRequest extends PostTweetRequest {
    private Optional<Long> statusID;

    @JsonProperty("statusID")
    public Optional<Long> getStatusID() {
        return statusID;
    }

    public void setStatusID(Optional<Long> statusID) {
        this.statusID = statusID;
    }
}

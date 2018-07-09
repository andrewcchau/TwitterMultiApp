package lithium.university.models;

import java.util.Date;
import java.util.Optional;

public class TwitterPost {
    private String twitterMessage;
    private TwitterUser user;
    private Date createdAt;

    public TwitterPost() {}

    public TwitterPost(String twitterMessage, TwitterUser user, Date createdAt) {
        this.twitterMessage = twitterMessage;
        this.user = user;
        this.createdAt = createdAt;
    }

    public Optional<String> getTwitterMessage() {
        return Optional.ofNullable(twitterMessage);
    }

    public void setTwitterMessage(String twitterMessage) {
        this.twitterMessage = twitterMessage;
    }

    public Optional<TwitterUser> getUser() {
        return Optional.ofNullable(user);
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public Optional<Date> getCreatedAt() {
        return Optional.ofNullable(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

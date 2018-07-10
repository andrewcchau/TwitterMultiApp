package lithium.university.models;

import java.util.Date;

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

    public String getTwitterMessage() {
        return twitterMessage;
    }

    public void setTwitterMessage(String twitterMessage) {
        this.twitterMessage = twitterMessage;
    }

    public TwitterUser getUser() {
        return user;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

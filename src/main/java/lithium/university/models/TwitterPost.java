package lithium.university.models;

import java.util.Date;
import java.util.Objects;

public class TwitterPost {
    private String twitterMessage;
    private TwitterUser user;
    private Date createdAt;
    private String statusId;

    public TwitterPost() {
    }

    public TwitterPost(String twitterMessage, TwitterUser user, Date createdAt, String statusId) {
        this.twitterMessage = twitterMessage;
        this.user = user;
        this.createdAt = createdAt;
        this.statusId = statusId;
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

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(twitterMessage, user, createdAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TwitterPost)) return false;

        TwitterPost tpObj = (TwitterPost) obj;
        return twitterMessage.equals(tpObj.twitterMessage) &&
                Objects.equals(user, tpObj.user) &&
                Objects.equals(createdAt, tpObj.createdAt);
    }

    @Override
    public String toString() {
        return "Twitter Message: '" + twitterMessage + "', Twitter User: [" + user.toString() +
                "], Created At: '" + createdAt.toString() + "'";
    }
}

package lithium.university.models;

public class TwitterUser {
    private String twitterHandle;
    private String name;
    private String profileImageURL;

    public TwitterUser() {}

    public TwitterUser(String twitterHandle, String name, String profileImageURL) {
        this.twitterHandle = twitterHandle;
        this.name = name;
        this.profileImageURL = profileImageURL;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}

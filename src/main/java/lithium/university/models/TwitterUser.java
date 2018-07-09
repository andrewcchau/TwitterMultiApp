package lithium.university.models;

import java.util.Optional;

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

    public Optional<String> getTwitterHandle() {
        return Optional.ofNullable(twitterHandle);
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getProfileImageURL() {
        return Optional.ofNullable(profileImageURL);
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}

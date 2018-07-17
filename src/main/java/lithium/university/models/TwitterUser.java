package lithium.university.models;

import java.util.Objects;

public class TwitterUser {
    private String twitterHandle;
    private String name;
    private String profileImageURL;

    public TwitterUser() {
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(twitterHandle, name, profileImageURL);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof TwitterUser)) return false;

        TwitterUser tuObj = (TwitterUser) obj;
        return twitterHandle.equals(tuObj.twitterHandle) &&
                name.equals(tuObj.name) &&
                profileImageURL.equals(tuObj.profileImageURL);
    }

    @Override
    public String toString() {
        return "Twitter Handle: '" + twitterHandle + "', Name: '" + name + "', Profile Image URL: '"
                + profileImageURL + "'";
    }
}

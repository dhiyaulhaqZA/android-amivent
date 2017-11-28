package id.ac.amikom.amivent.model;

/**
 * Created by dhiyaulhaqza on 11/28/17.
 */

public class User {
    private String noId;
    private String displayName;
    private String organizationName;
    private String photoUri;
    private String phoneNumber;

    public User() {
    }

    public User(String noId, String displayName, String organizationName, String photoUri, String phoneNumber) {
        this.noId = noId;
        this.displayName = displayName;
        this.organizationName = organizationName;
        this.photoUri = photoUri;
        this.phoneNumber = phoneNumber;
    }

    public String getNoId() {
        return noId;
    }

    public void setNoId(String noId) {
        this.noId = noId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

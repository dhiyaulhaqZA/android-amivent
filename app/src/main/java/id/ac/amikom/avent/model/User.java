package id.ac.amikom.avent.model;

/**
 * Created by dhiyaulhaqza on 11/28/17.
 */

public class User {
    private String noId;
    private String name;
    private String organizationName;
    private String phoneNumber;
    private String email;

    public User() {
    }

    public User(String noId, String name, String organizationName,
                String phoneNumber, String email) {
        this.noId = noId;
        this.name = name;
        this.organizationName = organizationName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getNoId() {
        return noId;
    }

    public void setNoId(String noId) {
        this.noId = ifNullThenEmpty(noId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = ifNullThenEmpty(name);
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = ifNullThenEmpty(organizationName);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = ifNullThenEmpty(phoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = ifNullThenEmpty(email);
    }

    private String ifNullThenEmpty(String str) {
        return str == null? "" : str;
    }
}

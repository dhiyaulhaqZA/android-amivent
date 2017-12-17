package id.ac.amikom.avent.model;

import java.util.List;

/**
 * Created by dhiyaulhaqza on 12/3/17.
 */

public class Event {
    private String posterUrl;
    private String title;
    private String organizer;
    private String description;
    private String location;
    private String locationDescription;
    private String latitude;
    private String longitude;
    private String contactPerson;
    private String date;
    private String startTime;
    private String endTime;
    private List<Participant> participants;

    public Event() {
    }

    public Event(String posterUrl, String title, String organizer, String description,
                 String locationDescription, String latitude, String longitude,
                 String contactPerson, String date, String startTime, String endTime,
                 List<Participant> participants) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.organizer = organizer;
        this.description = description;
        this.locationDescription = locationDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactPerson = contactPerson;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}

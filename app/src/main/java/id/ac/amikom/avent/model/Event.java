package id.ac.amikom.avent.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by dhiyaulhaqza on 12/3/17.
 */

public class Event implements Parcelable {
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
                 String location, String locationDescription, String latitude, String longitude,
                 String contactPerson, String date, String startTime, String endTime,
                 List<Participant> participants) {
        this.posterUrl = posterUrl;
        this.title = title;
        this.organizer = organizer;
        this.description = description;
        this.location = location;
        this.locationDescription = locationDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contactPerson = contactPerson;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
    }

    protected Event(Parcel in) {
        posterUrl = in.readString();
        title = in.readString();
        organizer = in.readString();
        description = in.readString();
        location = in.readString();
        locationDescription = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        contactPerson = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterUrl);
        dest.writeString(title);
        dest.writeString(organizer);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(locationDescription);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(contactPerson);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
    }
}

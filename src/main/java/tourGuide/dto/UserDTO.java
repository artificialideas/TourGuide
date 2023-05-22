package tourGuide.dto;

import gpsUtil.location.Location;

public class UserDTO {
    private String username;
    private String email;
    private Location latestUserLocation;
    private TripDTO tripDetails;

    /** Getters / Setters */
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public TripDTO getTripDetails() {
        return tripDetails;
    }
    public void setTripDetails(TripDTO tripDetails) {
        this.tripDetails = tripDetails;
    }
}

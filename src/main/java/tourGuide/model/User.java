package tourGuide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;

	private Date latestLocationTimestamp;

	private UserPreferences userPreferences = new UserPreferences();
	private final List<VisitedLocation> visitedLocations = new ArrayList<>();
	private final List<UserReward> userRewards = new ArrayList<>();
	private List<Provider> tripDeals = new ArrayList<>();

	/** Class constructors */
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	/** Getters / Setters */
	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}
	
	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}
	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}
	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	public List<UserReward> getUserRewards() {
		return userRewards;
	}
	public void addUserReward(UserReward userReward) {
		if (userRewards.stream()
				.noneMatch(r -> r.attraction.attractionName.equals(userReward.attraction.attractionName))) {
			userRewards.add(userReward);
		}
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}

	public List<Provider> getTripDeals() {
		return tripDeals;
	}
	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}
}

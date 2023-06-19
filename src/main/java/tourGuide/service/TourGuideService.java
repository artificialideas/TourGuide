package tourGuide.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import gpsUtil.location.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.dto.AttractionDTO;
import tourGuide.model.User;
import tourGuide.tracker.Tracker;

@Service
public class TourGuideService {
	@Autowired
	private GpsUtil gpsUtil;
	@Autowired
	private RewardCentral rewardCentral;
	@Autowired
	private RewardsService rewardsService;
	@Autowired
	private UserService userService;

	private final Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	public final Tracker tracker = new Tracker(this);
	boolean testMode = true;

	/** Class constructors */
	public TourGuideService() {
		// Start tracker
		tracker.startTracking();

		addShutDownHook();
	}

	public VisitedLocation getUserLocation(User user) {
		if (user.getVisitedLocations() != null) {
			return (user.getVisitedLocations().size() > 0) ?
					user.getLastVisitedLocation() :
					userService.trackUserLocation(user);
		} else {
			logger.error("User with username " + user.getUserName() + " doesn't exist.");
			return null;
		}
	}

	public Map<String, Location> getAllCurrentLocations() {
		// Get a list of every model's most recent location
		Map<String, Location> usersLocation = new HashMap<>();
		List<User> users = userService.getAllUsers();
		for (User user : users) {
			if (user.getUserId() != null && user.getLastVisitedLocation().location != null)
				usersLocation.put(user.getUserId().toString(), user.getLastVisitedLocation().location);
		}

		return usersLocation;
	}

	public List<AttractionDTO> getNearByAttractions(VisitedLocation visitedLocation) {
		// Sort list by distance from user
		gpsUtil.getAttractions().sort(
				Comparator.comparing(attraction -> rewardsService.isWithinUserProximity(attraction, visitedLocation.location))
		);

		// Get the closest five tourist attractions to the model - no matter how far away they are
		List<Attraction> attractionsList = gpsUtil.getAttractions()
				.stream()
				.limit(5)
				.collect(Collectors.toList());

		// Prepare DTO
		List<AttractionDTO> attractionDTOList = new ArrayList<>();
		for (Attraction attraction : attractionsList) {
			Location attractionDTOCoordinates = new Location(attraction.latitude, attraction.longitude);

			AttractionDTO attractionDTO = new AttractionDTO();
				attractionDTO.setAttractionName(attraction.attractionName);
				attractionDTO.setAttractionCoordinates(attractionDTOCoordinates);
				attractionDTO.setUserCoordinates(visitedLocation.location);
				attractionDTO.setDistance(rewardsService.getDistance(attractionDTOCoordinates, visitedLocation.location));
				attractionDTO.setRewardPoints(rewardCentral.getAttractionRewardPoints(attraction.attractionId, visitedLocation.userId));

			attractionDTOList.add(attractionDTO);
		}
		attractionDTOList.sort(Comparator.comparing(AttractionDTO::getDistance));

		return attractionDTOList;
	}

	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(tracker::stopTracking));
	}
}

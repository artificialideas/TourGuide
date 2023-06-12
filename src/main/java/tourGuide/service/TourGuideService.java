package tourGuide.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.model.User;
import tourGuide.tracker.Tracker;

@Service
public class TourGuideService extends InternalTestingService {
	@Autowired
	private GpsUtil gpsUtil;
	@Autowired
	private RewardsService rewardsService;
	@Autowired
	private UserService userService;

	private final Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	public final Tracker tracker = new Tracker(this);
	boolean testMode = true;

	/** Class constructors */
	public TourGuideService() {
		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}

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

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}
		
		return nearbyAttractions;
	}
	
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		});
	}
}

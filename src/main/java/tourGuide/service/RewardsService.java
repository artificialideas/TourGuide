package tourGuide.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import tourGuide.model.User;
import tourGuide.model.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	// proximity in miles
    private final int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;

	private final Logger logger = LoggerFactory.getLogger(RewardsService.class);

	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;

	/** Class constructors */
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	public void calculateRewards(User user) {
		// Adjusts the ThreadPool number of threads by reusing the unused ones
		ExecutorService executor = Executors.newCachedThreadPool();

		// Run the thread separately and notify about any failure that may occur
		CompletableFuture.runAsync(() -> {
			List<VisitedLocation> userLocations = user.getVisitedLocations();
			List<Attraction> attractions = gpsUtil.getAttractions();

			// Long-running process
			getRewards(user, userLocations, attractions);
		}, executor)
		.handle((res, ex) -> {
			if (ex != null) {
				logger.error("Something went wrong with calculateRewards(): " + ex.getMessage());
			}
			return res;
		});

		executor.shutdown();
	}

	void getRewards(User user, List<VisitedLocation> userLocations, List<Attraction> attractions) {
		// Use parallel concurrency for process improvement
		userLocations
				.parallelStream()
				.forEach(visitedLocation -> {
			attractions
					.parallelStream()
					.forEach(attraction -> {
				// Work concurrently
				if (user.getUserRewards()
						.stream()
						.noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
					if (nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			});
		});
	}

	public int isWithinUserProximity(Attraction attraction, Location location) {
		return (int) Math.round(getDistance(attraction, location));
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
	}
	
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}
}

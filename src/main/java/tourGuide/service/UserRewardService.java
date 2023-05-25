package tourGuide.service;

import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;

@Service
public class UserRewardService {
    @Autowired
    private UserService userService;

    private final TripPricer tripPricer = new TripPricer();
    private static final String tripPricerApiKey = "test-server-api-key";

    public List<UserReward> getUserRewards(User user) {
        return user.getUserRewards();
    }

    public VisitedLocation getUserLocation(User user) {
        return (user.getVisitedLocations().size() > 0) ?
                user.getLastVisitedLocation() :
                userService.trackUserLocation(user);
    }

    public List<Provider> getTripDeals(User user) {
        int cumulativeRewardPoints = user.getUserRewards()
                .stream()
                .mapToInt(UserReward::getRewardPoints)
                .sum();
        List<Provider> providers = tripPricer.getPrice(
                tripPricerApiKey,
                user.getUserId(),
                user.getUserPreferences().getNumberOfAdults(),
                user.getUserPreferences().getNumberOfChildren(),
                user.getUserPreferences().getTripDuration(),
                cumulativeRewardPoints);
        user.setTripDeals(providers);

        return providers;
    }
}

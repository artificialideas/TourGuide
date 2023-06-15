package tourGuide.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import tourGuide.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private Map<String, User> userMap; // We use our DataLoader bean for false data testing
    @Autowired
    private GpsUtil gpsUtil;
    @Autowired
    private RewardsService rewardsService;
    @Autowired
    private InternalTestingService internalTestingService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getUser(String userName) {
        return userMap.get(userName);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public void addUser(User user) {
        if (!userMap.containsKey(user.getUserName())) {
            userMap.put(user.getUserName(), user);
        } else
            logger.error(user.getUserName() + " already exists.");
    }

    public VisitedLocation trackUserLocation(User user) {
        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user);

        return visitedLocation;
    }
}

package service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.After;
import org.junit.Before;
import tourGuide.Application;
import tourGuide.model.User;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserReward;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserRewardService;
import tourGuide.service.UserService;
import tripPricer.Provider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
public class UserRewardServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRewardService userRewardService;
    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private GpsUtil gpsUtil;

    private User user;

    @Before
    public void setUp() {
        InternalTestHelper.setInternalUserNumber(0);
        user = userService.getAllUsers().get(0);

        VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
        Attraction attraction = gpsUtil.getAttractions().get(0);
        UserReward userReward = new UserReward(visitedLocation, attraction, 5);
        user.addUserReward(userReward);
        
        //tourGuideService.tracker.startTracking();
    }

    @After
    public void tearDown() {
        tourGuideService.tracker.stopTracking();
    }

    @Test
    public void getUserRewards() {
        List<UserReward> rewards = userRewardService.getUserRewards(user);

        assertEquals(1, rewards.size());
    }

    @Test
    public void getTripDeals() {
        List<Provider> providers = userRewardService.getTripDeals(user);

        assertEquals(5, providers.size());
    }
}

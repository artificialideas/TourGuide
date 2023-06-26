package service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.model.UserReward;
import tourGuide.service.UserRewardService;
import tourGuide.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
public class RewardsServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRewardService userRewardService;
	@Autowired
	private TourGuideService tourGuideService;
	@Autowired
	private GpsUtil gpsUtil;
	@Autowired
	private RewardsService rewardsService;

	private User user;
	private Attraction attraction;

	@Before
	public void setUp() {
		attraction = gpsUtil.getAttractions().get(0);
		user = userService.getAllUsers().get(0);
		userService.addUser(user);

		//tourGuideService.tracker.startTracking();
	}

	@After
	public void tearDown() {
		tourGuideService.tracker.stopTracking();
	}

	@Test
	public void userGetRewards() {
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		userService.trackUserLocation(user);

		List<UserReward> userRewards = user.getUserRewards();

		assertEquals(1, userRewards.size());
	}
	
	@Test
	public void isWithinUserProximity() {
		assertTrue(rewardsService.isWithinUserProximity(attraction, user.getLastVisitedLocation().location) > 0);
	}


	@Test
	public void nearAllAttractions() {
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		InternalTestHelper.setInternalUserNumber(1);

		rewardsService.calculateRewards(user);
		List<UserReward> userRewards = user.getUserRewards();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}
}

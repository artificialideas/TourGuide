package service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.service.UserRewardService;
import tourGuide.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

	@Before
	public void setUp() {
		InternalTestHelper.setInternalUserNumber(1);
		user = userService.getAllUsers().get(0);
	}

	@After
	public void tearDown() {
		tourGuideService.tracker.stopTracking();
	}

	@Test
	public void userGetRewards() throws InterruptedException, ExecutionException {
		CompletableFuture<VisitedLocation> futureResult = userService.trackUserLocation(user);

		// Wait for the trackUserLocation() to complete
		VisitedLocation result = futureResult.get();
		assertNotNull(result);

		while (user.getUserRewards().size() == 0) {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException ex) {
				System.out.println("Something went wrong: " + ex);
			}

			assertTrue(user.getUserRewards().size() > 0);
		}
	}
	
	@Test
	public void isWithinUserProximity() {
		Attraction attraction = gpsUtil.getAttractions().get(0);

		assertTrue(rewardsService.isWithinUserProximity(attraction, user.getLastVisitedLocation().location) > 0);
	}


	@Test
	public void nearAllAttractions() {
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		rewardsService.calculateRewards(user);

		while (user.getUserRewards().size() == 0) {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException ex) {
				System.out.println("Something went wrong: " + ex);
			}

			assertTrue(user.getUserRewards().size() > 0);
		}
	}
}

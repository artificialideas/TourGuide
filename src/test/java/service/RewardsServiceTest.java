package service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.service.UserRewardService;
import tourGuide.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

	@BeforeEach
	public void setUp() {
		InternalTestHelper.setInternalUserNumber(1);
		user = userService.getAllUsers().get(0);
	}

	@AfterEach
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

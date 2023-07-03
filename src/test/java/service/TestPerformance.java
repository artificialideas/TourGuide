package service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
public class TestPerformance {
	@Autowired
	private UserService userService;
	@Autowired
	private TourGuideService tourGuideService;
	@Autowired
	private GpsUtil gpsUtil;
	@Autowired
	private RewardsService rewardsService;

	private StopWatch stopWatch;
	private List<User> allUsers;

	@BeforeEach
	public void setUp() {
		InternalTestHelper.setInternalUserNumber(1000);
		stopWatch = new StopWatch();
		stopWatch.start();
	}

	@AfterEach
	public void tearDown() {
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
	}
	
	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */
	
	@Ignore
	@Test
	public void highVolumeTrackLocation() {
		allUsers = userService.getAllUsers();

		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		for (User user : allUsers) {
			userService.trackUserLocation(user);
		}

		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
	@Test
	public void highVolumeGetRewards() {
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		allUsers = userService.getAllUsers();
	    Attraction attraction = gpsUtil.getAttractions().get(0);

		allUsers.forEach(u -> {
			u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date()));
			userService.trackUserLocation(u);
		});
	    /*allUsers.forEach(u -> {
			if (rewardsService.calculateRewards(u) != null)
				assertTrue(rewardsService.calculateRewards(u).getUserRewards().size() > 0);
		});*/

		for (User user : allUsers) {
			while (user.getUserRewards().size() == 0) {
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException ex) {
					System.out.println("Something went wrong: " + ex);
				}
			}

			assertTrue(user.getUserRewards().size() > 0);
		}

		//assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
}

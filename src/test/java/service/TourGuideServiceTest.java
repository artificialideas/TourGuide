package service;
import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.service.UserService;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
public class TourGuideServiceTest {
	@Autowired
	private UserService userService;
	@Autowired
	private TourGuideService tourGuideService;

	private User user;

	@Before
	public void setUp() {
		InternalTestHelper.setInternalUserNumber(0);
		user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		userService.addUser(user);
		tourGuideService.tracker.startTracking();
	}

	@After
	public void tearDown() {
		tourGuideService.tracker.stopTracking();
	}
	
	@Test
	public void trackUser() {
		VisitedLocation visitedLocation = userService.trackUserLocation(user);
		
		assertEquals(user.getUserId(), visitedLocation.userId);
	}
	
	@Ignore // Not yet implemented
	@Test
	public void getNearbyAttractions() {
		VisitedLocation visitedLocation = userService.trackUserLocation(user);
		
		List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);
		
		assertEquals(5, attractions.size());
	}
}

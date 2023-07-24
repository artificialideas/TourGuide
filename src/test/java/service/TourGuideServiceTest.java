package service;

import gpsUtil.location.Location;
import tourGuide.Application;
import tourGuide.dto.AttractionDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.service.UserService;
import gpsUtil.location.VisitedLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
		InternalTestHelper.setInternalUserNumber(1);
		user = userService.getAllUsers().get(0);
	}

	@After
	public void tearDown() {
		tourGuideService.tracker.stopTracking();
	}
	
	@Test
	public void trackUser() throws ExecutionException, InterruptedException {
		CompletableFuture<VisitedLocation> futureResult = userService.trackUserLocation(user);

		// Wait for the trackUserLocation() to complete
		VisitedLocation result = futureResult.get();
		assertNotNull(result);
		
		assertEquals(user.getUserId(), result.userId);
	}

	@Test
	public void getUserLocation() throws ExecutionException, InterruptedException {
		CompletableFuture<VisitedLocation> futureResult = userService.trackUserLocation(user);

		// Wait for the trackUserLocation() to complete
		VisitedLocation result = futureResult.get();
		assertNotNull(result);

		assertEquals(result.userId, user.getUserId());
	}

	@Test
	public void getAllCurrentLocations() {
		Map<String, Location> usersLocation = tourGuideService.getAllCurrentLocations();

		assertTrue(usersLocation.size() > 0);
	}
	
	@Test
	public void getNearbyAttractions() throws ExecutionException, InterruptedException {
		CompletableFuture<VisitedLocation> futureResult = userService.trackUserLocation(user);

		// Wait for the trackUserLocation() to complete
		VisitedLocation result = futureResult.get();
		assertNotNull(result);
		
		List<AttractionDTO> attractions = tourGuideService.getNearByAttractions(result);
		
		assertTrue(attractions.size() > 0);
	}
}

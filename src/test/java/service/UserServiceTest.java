package service;

import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.service.TourGuideService;
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
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={Application.class})
public class UserServiceTest {
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
        //tourGuideService.tracker.startTracking();
    }

    @After
    public void tearDown() {
        tourGuideService.tracker.stopTracking();
    }

    @Test
    public void getAllUsers() {
        List<User> allUsers = userService.getAllUsers();

        assertTrue(allUsers.stream().map(User::getUserName).anyMatch(user.getUserName()::equals));
    }

    @Test
    public void addUser() {
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");
        userService.addUser(user2);

        User retrivedUser2 = userService.getUser(user2.getUserName());

        assertEquals(user2, retrivedUser2);
    }
}

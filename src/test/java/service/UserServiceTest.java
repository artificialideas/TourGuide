package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes={Application.class})
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private TourGuideService tourGuideService;

    private User user;

    @BeforeEach
    public void setUp() {
        InternalTestHelper.setInternalUserNumber(0);
        user = userService.getAllUsers().get(0);
        userService.addUser(user);
    }

    @AfterEach
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

package service;

import tourGuide.Application;
import tourGuide.model.User;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserRewardService;
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
    private UserRewardService userRewardService;
    @Autowired
    private TourGuideService tourGuideService;

    @Test
    public void getTripDeals() {
        InternalTestHelper.setInternalUserNumber(0);

        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        List<Provider> providers = userRewardService.getTripDeals(user);

        tourGuideService.tracker.stopTracking();

        assertEquals(10, providers.size());
    }
}

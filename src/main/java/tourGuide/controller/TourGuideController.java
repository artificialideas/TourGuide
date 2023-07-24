package tourGuide.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import gpsUtil.location.Attraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.service.UserRewardService;
import tourGuide.service.UserService;
import tripPricer.Provider;

@RestController
public class TourGuideController {
	@Autowired
    UserService userService;
    @Autowired
    UserRewardService userRewardService;
    @Autowired
    TourGuideService tourGuideService;
	
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }
    
    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
        try {
            VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
            return JsonStream.serialize(visitedLocation.location);
        } catch (NullPointerException ex) {
            return ("User with username " + userName + " doesn't exist.");
        } catch (ExecutionException | InterruptedException ex) {
            return ("Error with completable future: " + ex);
        }
    }

    @RequestMapping("/getNearbyAttractions") 
    public String getNearbyAttractions(@RequestParam String userName) {
        try {
            VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
            return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
        } catch (NullPointerException ex) {
            return ("User with username " + userName + " doesn't exist.");
        } catch (ArithmeticException ex) {
            return ("Unable to get " + userName + " location.");
        } catch (ExecutionException | InterruptedException ex) {
            return ("Error with completable future: " + ex);
        }
    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
        try {
    	    return JsonStream.serialize(userRewardService.getUserRewards(getUser(userName)));
        } catch (NullPointerException ex) {
            return ("User with username " + userName + " doesn't exist.");
        }
    }
    
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
        try {
    	    return JsonStream.serialize(tourGuideService.getAllCurrentLocations());
        } catch (RuntimeException ex) {
            return ("Unable to get all locations.");
        }
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        try {
            List<Provider> providers = userRewardService.getTripDeals(getUser(userName));
            return JsonStream.serialize(providers);
        } catch (NullPointerException ex) {
            return ("User with username " + userName + " doesn't exist.");
        }
    }
    
    private User getUser(String userName) {
        return userService.getUser(userName);
    }
}
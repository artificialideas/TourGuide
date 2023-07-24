package controller;

import org.junit.jupiter.api.BeforeEach;
import tourGuide.Application;
import tourGuide.model.User;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes={Application.class})
@AutoConfigureMockMvc
public class TourGuideControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void setUp() {
        InternalTestHelper.setInternalUserNumber(1);
        user = userService.getAllUsers().get(0);
    }

    @Test
    @DisplayName("Index //index()")
    public void index() throws Exception {
        mockMvc.perform( get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET - returns user 2 coordinates //getLocation()")
    public void givenUser_whenUserName_shouldReturnUserLocation() throws Exception {
        mockMvc.perform( get("/getLocation")
                        .param("userName", user.getUserName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    @DisplayName("GET - returns list of 5 attractions nearby //getNearbyAttractions()")
    public void givenUser_whenUserName_shouldReturnListOfNearbyAttractions() throws Exception {
        mockMvc.perform( get("/getNearbyAttractions")
                        .param("userName", user.getUserName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }

    @Test
    @DisplayName("GET - returns list of rewards //getRewards()")
    public void givenUser_whenUserName_shouldReturnListOfRewards() throws Exception {
        mockMvc.perform( get("/getRewards")
                        .param("userName", user.getUserName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    @DisplayName("GET - returns list of all users locations (100) //getAllCurrentLocations()")
    public void givenEndpoint_shouldReturnListOfAllCurrentLocations() throws Exception {
        mockMvc.perform( get("/getAllCurrentLocations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(100)));
    }

    @Test
    @DisplayName("GET - returns list of 5 providers //getTripDeals()")
    public void givenUser_whenUserName_shouldReturnListOfProvidersWithDeals() throws Exception {
        mockMvc.perform(get("/getTripDeals")
                        .param("userName", user.getUserName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(5)));
    }
}

package controller;

import tourGuide.Application;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.User;
import tourGuide.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={Application.class})
@AutoConfigureMockMvc
public class TourGuideControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @Before
    public void setUp() {
        InternalTestHelper.setInternalUserNumber(0);
        user = userService.getAllUsers().get(0);
    }

    @Test
    @DisplayName("GET - returns user location coordinates //getLocation()")
    public void givenUser_whenUserName_shouldReturnUserLocation() throws Exception {
        mockMvc.perform( get("/getLocation" + user.getUserName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}

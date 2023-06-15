package tourGuide.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import tourGuide.model.User;
import tourGuide.service.InternalTestingService;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataLoader {
    @Autowired
    private InternalTestingService internalTestingService;

    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);
    boolean testMode = true;
    public Map<String, User> usersMap = new HashMap<>();

    @Bean
    public Map<String, User> getTestUsersMap() {
        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            usersMap = internalTestingService.initializeInternalUsers();
            logger.debug("Finished initializing users");
        }

        return usersMap;
    }
}

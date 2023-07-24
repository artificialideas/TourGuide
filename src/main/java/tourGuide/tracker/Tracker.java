package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import tourGuide.service.TourGuideService;
import tourGuide.model.User;
import tourGuide.service.UserService;

public class Tracker extends Thread {
	@Autowired
	private UserService userService;
	@Autowired
	private TourGuideService tourGuideService;

	private final Logger logger = LoggerFactory.getLogger(Tracker.class);

	private static final long trackingPollingInterval = TimeUnit.SECONDS.toSeconds(20);
	private boolean stop = false;
	private final ExecutorService executorService = Executors.newSingleThreadExecutor();

	/** Class constructors */
	public Tracker(TourGuideService tourGuideService) {}

	/**
	 * Assures to shut down the Tracker thread
	 */
	public void stopTracking() {
		stop = true;
		executorService.shutdownNow();
	}

	/**
	 * Tracker launcher
	 */
	@Override
	public void run() {
		// Time counter
		StopWatch stopWatch = new StopWatch();

		while(true) {
			if (Thread.currentThread().isInterrupted() || stop) {
				logger.debug("Tracker stopping");
				break;
			}

			// Get all users
			List<User> users = userService.getAllUsers();
			// Start Tracker
			stopWatch.start();

			users.forEach(userService::trackUserLocation);
			logger.debug("Begin Tracker. Tracking " + users.size() + " users.");

			// Stop Tracker
			stopWatch.stop();
			logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");

			// Reset Tracker
			stopWatch.reset();
			try {
				logger.debug("Tracker sleeping");
				TimeUnit.SECONDS.sleep(trackingPollingInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
		
	}
}

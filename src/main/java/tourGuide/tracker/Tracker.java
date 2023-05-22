package tourGuide.tracker;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tourGuide.service.TourGuideService;
import tourGuide.model.User;

public class Tracker extends Thread {
	private final Logger logger = LoggerFactory.getLogger(Tracker.class);

	private static final long trackingPollingInterval = TimeUnit.SECONDS.toSeconds(20);
	private boolean stop = false;

	private final ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final TourGuideService tourGuideService;


	/** Class constructors */
	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;
	}

	/**
	 * Start the tracker
	 */
	public void startTracking() {
		stop = false;
		executorService.submit(this);
	}

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

			// Start Tracker
			stopWatch.start();

			// Get all users
			List<User> users = tourGuideService.getAllUsers();
			users.forEach(tourGuideService::trackUserLocation);
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

package tourGuide.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private final ExecutorService executor;

    public ThreadPoolManager(double percentage) {
        int numThreads = getDesiredThreadCount(percentage);
        executor = createThreadPool(numThreads);
    }

    public void submitTask(Runnable task) {
        executor.submit(task);
    }

    public void shutdown() {
        executor.shutdown();
    }

    private ExecutorService createThreadPool(int numThreads) {
        return Executors.newFixedThreadPool(numThreads);
    }

    private int getDesiredThreadCount(double percentage) {
        // Get the number of available processors (cores)
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        // Calculate the number of threads based on the specified percentage
        return (int) Math.ceil(availableProcessors * percentage);
    }
}

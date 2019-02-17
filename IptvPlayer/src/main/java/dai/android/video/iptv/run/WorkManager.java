package dai.android.video.iptv.run;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class WorkManager {

    private static WorkManager sWorkManager = null;

    public static WorkManager get() {
        if (null == sWorkManager) {
            synchronized (WorkManager.class) {
                if (null == sWorkManager) {
                    sWorkManager = new WorkManager();
                }
            }
        }
        return sWorkManager;
    }

    private final ExecutorService mExecutor;

    private WorkManager() {
        int pollSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        mExecutor = Executors.newFixedThreadPool(pollSize);
    }

    public void shutdown() {
        mExecutor.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return mExecutor.shutdownNow();
    }

    public boolean isShutdown() {
        return mExecutor.isShutdown();
    }

    public boolean isTerminated() {
        return mExecutor.isTerminated();
    }

    public <T> Future<T> submit(Callable<T> task) {
        if (null == task) {
            return null;
        }
        return mExecutor.submit(task);
    }

    public Future<?> submit(Runnable task) {
        if (null == task) {
            return null;
        }
        return mExecutor.submit(task);
    }

    public void execute(Runnable command) {
        if (null == command) {
            return;
        }
        mExecutor.execute(command);
    }
}

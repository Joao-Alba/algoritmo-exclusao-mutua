import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CheckResourceAccessRunnable implements Runnable {

    private final Resource resource;
    private final Queue<AtomicBoolean> accessQueue;

    private static final Lock resourceCheckLock = new ReentrantLock();

    public CheckResourceAccessRunnable(Resource resource, Queue<AtomicBoolean> accessQueue) {
        this.resource = resource;
        this.accessQueue = accessQueue;
    }

    public void run() {
        try {
            while (true) {
                resourceCheckLock.lock();
                if (!resource.isOccupied()) {
                    accessQueue.remove().set(true);
                    break;
                }
                resourceCheckLock.unlock();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            resourceCheckLock.unlock();
        }
    }
}

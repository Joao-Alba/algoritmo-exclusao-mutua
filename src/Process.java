import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Process implements Runnable {

    private final Long id;
    private boolean isCoordinator = false;
    private final HashMap<Long, Queue<AtomicBoolean>> resourceAccessQueue = new HashMap<>();

    private static final Random rand = new Random();
    private final int accessResourceCooldown = rand.nextInt(MIN_PROCESS_COOLDOWN, MAX_PROCESS_COOLDOWN);

    private static final int MIN_PROCESS_COOLDOWN = 10000;
    private static final int MAX_PROCESS_COOLDOWN = 25000;

    public Process(Long id) {
        this.id = id;
    }

    public void run() {
        this.fillResourceAccessQueue();
        try {
            while (true) {
                Thread.sleep(accessResourceCooldown);
                int resourceToUse = rand.nextInt(Main.resourceList.size());
                tryToAccessResource(Main.resourceList.get(resourceToUse));

            }
        } catch (InterruptedException ignored) {
        }
    }

    private Optional<ThreadProcess> checkCoordinator() {
        if (Main.threadProcessList.stream().noneMatch(threadProcess -> threadProcess.process().isCoordinator)) {
            return Main.startElection(this);
        }

        return Main.threadProcessList.stream().filter(threadProcess -> threadProcess.process().isCoordinator).findFirst();
    }

    private void tryToAccessResource(Resource resource) throws InterruptedException {
        System.out.println("Processo " + this.getId() + " quer acessar o recurso " + resource.getId());
        Optional<ThreadProcess> optCoordinator = this.checkCoordinator();

        if (optCoordinator.isEmpty()) {
            return;
        }

        AtomicBoolean resourceFree = optCoordinator.get().process().createFlag(resource);

        while (true) {
            if (resourceFree.get()) {
                System.out.println("Processo " + this.getId() + " ganhou acesso ao recurso " + resource.getId());
                String retorno = resource.useResource();
                System.out.println("Processo " + this.getId() + " acessou o recurso " + resource.getId() + ". Retorno: " + retorno);
                break;
            }
        }
    }

    public AtomicBoolean createFlag(Resource resource) {
        Queue<AtomicBoolean> accessQueue = this.resourceAccessQueue.get(resource.getId());

        AtomicBoolean resourceFree = new AtomicBoolean(false);
        accessQueue.add(resourceFree);

        CheckResourceAccessRunnable checkRunnable = new CheckResourceAccessRunnable(resource, accessQueue);
        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

        return resourceFree;
    }

    private void fillResourceAccessQueue() {
        Main.resourceList.forEach(resource -> this.resourceAccessQueue.put(resource.getId(), new LinkedList<>()));
    }

    public Long getId() {
        return id;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void becomeCoordinator() {
        this.isCoordinator = true;
    }
}

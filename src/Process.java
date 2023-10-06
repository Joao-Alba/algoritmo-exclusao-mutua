import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Process implements Runnable{

    private final Long id;
    private boolean isCoordinator = false;
    private final int accessResourceCooldown = rand.nextInt(10000, 25000);
    private final HashMap<Long, Queue<ThreadProcess>> resourceAccessQueue = new HashMap<>();

    private static final Random rand = new Random();

    public Process(Long id){
        this.id = id;

        if(checkCoordinator().isEmpty()){
            this.becomeCoordinator();
        }
    }

    public void run(){
        int accessResourceCooldown = rand.nextInt(10000, 25000);
        this.fillResourceAccessQueue();
        try{
            while(true){
                Thread.sleep(accessResourceCooldown);
                int resourceToUse = rand.nextInt(Main.resourceList.size());
                tryToAccessResource(Main.resourceList.get(resourceToUse));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<ThreadProcess> checkCoordinator(){
        System.out.println("Checando coordenador");
        if(Main.threadProcessList.stream().noneMatch(threadProcess -> threadProcess.getProcess().isCoordinator)){
            Main.startElection(this);
        }

        return Main.threadProcessList.stream().filter(threadProcess -> threadProcess.getProcess().isCoordinator).findFirst();
    }

    private void tryToAccessResource(Resource resource) throws InterruptedException {
        System.out.println("Processo " + this.getId() + " quer acessar o recurso " + resource.getId());
        Optional<ThreadProcess> optCoordinator = this.checkCoordinator();

        if(optCoordinator.isEmpty()){
            return;
        }
        System.out.println("thread request: " + Main.threadProcessList.stream().filter(tp -> tp.getProcess().getId().equals(this.getId())).findFirst().get().getThread().getId());
        optCoordinator.get().getProcess().requestAccess(resource, this);
        this.wait();
        System.out.println("Processo " + this.getId() + " accessou o recurso " + resource.getId());
    }

    public void requestAccess(Resource resource, Process process) throws InterruptedException {
        Queue<ThreadProcess> accessQueue = this.resourceAccessQueue.get(resource.getId());
        ThreadProcess threadProcess = Main.threadProcessList.stream().filter(tp -> tp.getProcess().getId().equals(process.getId())).findFirst().get();
        System.out.println("thread found: " + threadProcess.getThread().getId());
        System.out.println("thread coord: " + Main.threadProcessList.stream().filter(tp -> tp.getProcess().getId().equals(this.getId())).findFirst().get().getThread().getId());

        System.out.println("Fila para o recurso " + resource.getId() + ": " + Arrays.toString(accessQueue.toArray()));

        if(accessQueue.isEmpty() && !resource.isOccupied()){
            threadProcess.getThread().notify();
            return;
        }

        accessQueue.add(threadProcess);

        CheckResourceAccessRunnable checkRunnable = new CheckResourceAccessRunnable(resource);
        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
        checkThread.join();
        accessQueue.remove().notify();
    }

    private void fillResourceAccessQueue(){
        Main.resourceList.forEach(resource -> this.resourceAccessQueue.put(resource.getId(), new LinkedList<>()));
    }

    public Long getId() {
        return id;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void becomeCoordinator(){
        this.isCoordinator = true;
    }
}

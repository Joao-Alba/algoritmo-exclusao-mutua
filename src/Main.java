import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static List<ThreadProcess> threadProcessList = new ArrayList<>();
    public static List<Resource> resourceList = new ArrayList<>();
    public static final Lock idLock = new ReentrantLock(true);
    public static final List<Long> availableIdList = new ArrayList<>();

    private static final int MAX_ID = 10000;

    public static void main(String[] args) {
        fillIdList();

        Resource r1 = new Resource(1L);
        Resource r2 = new Resource(2L);
        Resource r3 = new Resource(3L);
        resourceList.add(r1);
        resourceList.add(r2);
        resourceList.add(r3);

        CreateProcessRunnable createProcessRunnable = new CreateProcessRunnable();
        Thread createProcessThread = new Thread(createProcessRunnable);
        createProcessThread.start();

        KillCoordinatorRunnable killCoordinatorRunnable = new KillCoordinatorRunnable();
        Thread killCoordinatorThread = new Thread(killCoordinatorRunnable);
        killCoordinatorThread.start();
    }

    private static void fillIdList() {
        for (long i = 0L; i < MAX_ID; i++) {
            availableIdList.add(i);
        }
    }

    public static Optional<ThreadProcess> startElection(Process process) {
        System.out.println("Coordenador não encontrado, processo #" + process.getId() + " iniciou eleição");
        Optional<ThreadProcess> highestIdProcess = threadProcessList.stream().max(Comparator.comparing(threadProcess -> threadProcess.process().getId()));

        if (highestIdProcess.isPresent()) {
            highestIdProcess.get().process().becomeCoordinator();
            System.out.println("Processo #" + highestIdProcess.get().process().getId() + " se tornou o novo coordenador");
            return highestIdProcess;
        }

        return Optional.empty();
    }
}
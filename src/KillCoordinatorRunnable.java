import java.util.Optional;

public class KillCoordinatorRunnable implements Runnable {

    public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ignored) {
            }

            Optional<ThreadProcess> coordinatorThreadProcessOpt = Main.threadProcessList.stream().filter(threadProcess -> threadProcess.process().isCoordinator()).findFirst();

            coordinatorThreadProcessOpt.ifPresent(coordinator -> {
                coordinator.thread().interrupt();
                Main.threadProcessList.remove(coordinator);
                System.out.println("Coordenador interrompido: #" + coordinator.process().getId());
            });
        }
    }
}

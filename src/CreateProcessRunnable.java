import java.util.Random;

public class CreateProcessRunnable implements Runnable {

    private static final Random rand = new Random();

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }

            Process process = new Process(this.generateProcessId());
            Thread thread = new Thread(process);
            thread.start();
            Main.threadProcessList.add(new ThreadProcess(thread, process));

            System.out.println("Processo criado: #" + process.getId());
        }
    }

    private Long generateProcessId() {
        try {
            Main.idLock.lock();
            int index = rand.nextInt(Main.availableIdList.size());
            return Main.availableIdList.remove(index);
        } finally {
            Main.idLock.unlock();
        }
    }
}

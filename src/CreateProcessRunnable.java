import java.util.Random;

public class CreateProcessRunnable implements Runnable{

    private static final Random rand = new Random();

    public void run(){
        while(true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(Main.threadProcessList.size() == 2){
                break;
            }

            Process process = new Process(this.generateProcessId());
            Thread processThread = new Thread(process);
            processThread.start();
            Main.threadProcessList.add(new ThreadProcess(processThread, process));

            System.out.println("Processo criado: #" + process.getId());
        }
    }

    private Long generateProcessId(){
        try{
            Main.idLock.lock();
            int index = rand.nextInt(Main.availableIdList.size());
            return Main.availableIdList.remove(index);
        }finally {
            Main.idLock.unlock();
        }

    }
}

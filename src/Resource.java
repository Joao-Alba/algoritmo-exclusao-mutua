import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Resource {

    private static final Random rand = new Random();
    private final Long id;
    private boolean ocuppied = false;

    public String useResource() throws InterruptedException {
        this.setOcuppied(true);
        int waitTime = rand.nextInt(20000, 30000);
        Thread.sleep(waitTime);
        this.setOcuppied(false);
        return "Recurso retornado";
    }

    public Resource(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isOcuppied() {
        return ocuppied;
    }

    public void setOcuppied(boolean ocuppied) {
        this.ocuppied = ocuppied;
    }
}

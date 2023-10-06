import java.util.Random;

public class Resource {

    private static final Random rand = new Random();
    private final Long id;
    private boolean occupied = false;

    public String useResource() throws InterruptedException {
        this.setOccupied(true);
        int waitTime = rand.nextInt(5000, 15000);
        Thread.sleep(waitTime);
        this.setOccupied(false);
        return "Recurso usado";
    }

    public Resource(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}

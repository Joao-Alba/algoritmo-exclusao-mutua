import java.util.Random;

public class Resource {

    private static final Random rand = new Random();
    private final Long id;
    private boolean occupied = false;

    private final static int MIN_RESOURCE_TIME = 5000;
    private final static int MAX_RESOURCE_TIME = 15000;

    public String useResource() throws InterruptedException {
        try {
            this.setOccupied(true);
            int waitTime = rand.nextInt(MIN_RESOURCE_TIME, MAX_RESOURCE_TIME);
            Thread.sleep(waitTime);
            return "Recurso usado";
        } finally {
            this.setOccupied(false);
        }
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

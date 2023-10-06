public class CheckResourceAccessRunnable implements Runnable{

    private final Resource resource;

    public CheckResourceAccessRunnable(Resource resource) {
        this.resource = resource;
    }

    public void run(){
        try{
            while (true){
                if(!resource.isOccupied()){
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

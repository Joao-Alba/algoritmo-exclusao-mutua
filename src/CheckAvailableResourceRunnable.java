import java.util.concurrent.atomic.AtomicBoolean;

public class CheckAvailableResourceRunnable implements Runnable{

    private final Resource resource;
    private final AtomicBoolean accessPass;

    public CheckAvailableResourceRunnable(Resource resource, AtomicBoolean accessPass) {
        this.resource = resource;
        this.accessPass = accessPass;
    }

    public void run(){
        while(true){
            if(!resource.isOcuppied()){
                System.out.println("Acesso permitido");
                accessPass.set(true);
                break;
            }
        }
    }

    public Resource getResource() {
        return resource;
    }
}

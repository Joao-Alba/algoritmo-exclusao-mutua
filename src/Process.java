import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Process implements Runnable{

    private final Long id;
    private boolean isCoordinator = false;
    private final int accessResourceCooldown = rand.nextInt(10000, 25000);

    private static final Random rand = new Random();

    public Process(Long id){
        this.id = id;

        if(checkCoordinator().isEmpty()){
            this.becomeCoordinator();
        }
    }

    public void run(){
        int accessResourceCooldown = rand.nextInt(10000, 25000);
        try{
            while(true){
                Thread.sleep(accessResourceCooldown);
                int resourceToUse = rand.nextInt(Main.resourceList.size());
                useResource(Main.resourceList.get(resourceToUse));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void useResource(Resource resource) throws InterruptedException {
        System.out.println("Processo " + getId() + " quer usar recurso " + resource.getId());
        Optional<ThreadProcess> optCoordinatorThreadProcess = checkCoordinator();

        AtomicBoolean acessPass = new AtomicBoolean(false);
        optCoordinatorThreadProcess.get().getProcess().askForAccess(acessPass, resource);
        while (!acessPass.get()){
            System.out.println("Esperando acesso");
            Thread.sleep(100);
        }

        String retorno = resource.useResource();
        System.out.println(retorno);
    }

    private Optional<ThreadProcess> checkCoordinator(){
        System.out.println("Checando coordenador");
        if(Main.threadProcessList.stream().noneMatch((threadProcess -> threadProcess.getProcess().isCoordinator()))){
            Main.startElection(this);
        }

        return Main.threadProcessList.stream().filter((threadProcess -> threadProcess.getProcess().isCoordinator())).findFirst();
    }

    public void askForAccess(AtomicBoolean accessPass, Resource resource){
        System.out.println("Managing acesso");
        Optional<Resource> optResource = Main.resourceList.stream().filter(resource1 -> resource1.getId().equals(resource.getId())).findFirst();

        if(optResource.isEmpty()){
            return;
        }

        CheckAvailableResourceRunnable runnable = new CheckAvailableResourceRunnable(resource, accessPass);
        Thread thread = new Thread(runnable);
        thread.start();
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

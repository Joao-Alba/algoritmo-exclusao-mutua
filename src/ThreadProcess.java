public class ThreadProcess {

    private final Thread thread;
    private final Process process;

    public ThreadProcess(Thread thread, Process process) {
        this.thread = thread;
        this.process = process;
    }

    public Thread getThread() {
        return thread;
    }

    public Process getProcess() {
        return process;
    }
}
package manager.exeptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
        System.out.println(message);
    }
}
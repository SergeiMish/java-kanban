package manager.exeptions;

public class TaskConflictException extends RuntimeException {
    public TaskConflictException(String message) {
        super(message);
        System.out.println(message);
    }
}


package manager.exeptions;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
        System.out.println(message);
    }
}

package manager;

public class Managers {
    public static TaskManager getDefault() {
        // Верните реализацию TaskManager по умолчанию
        return new InMemoryTaskManager();
    }
}

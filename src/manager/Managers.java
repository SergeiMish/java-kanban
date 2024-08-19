package manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        // Верните реализацию TaskManager по умолчанию
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}

package manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return new InMemoryTaskManager(historyManager);

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


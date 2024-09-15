package manager;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int maxSizeInMemory = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() == maxSizeInMemory) {
            history.removeFirst();
        }
        history.add(task);
    }

    void remove(int id){
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}

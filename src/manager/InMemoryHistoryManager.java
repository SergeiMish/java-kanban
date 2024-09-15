package manager;

import Interfaces.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int MAX_SIZE_MEMORY = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() == MAX_SIZE_MEMORY) {
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

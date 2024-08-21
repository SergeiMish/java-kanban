package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> listSubTask = new ArrayList<>();

    public Epic(String name, String detail, Status status) {
        super(name, detail, status);
    }

    public void addSubTask(int sabTaskId) {
        listSubTask.add(sabTaskId);
    }

    public void removeSubTask(int subTaskId) {
        listSubTask.remove(Integer.valueOf(subTaskId));
    }

    public List<Integer> getListSubTask() {
        return new ArrayList<>(listSubTask);
    }

    public void removeAllSubtasks() {
        listSubTask.clear();
    }
}


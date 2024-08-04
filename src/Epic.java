import java.util.HashMap;

public class Epic extends Task {
    /**
     * Класс эпик наследуется от Task. В нем 2 мапы:
     * 1. Хранит ID и сабтаску
     * 2. Хранит ID и статус сабтаски
     * Содержит методы для работы с мапами.
     */
    HashMap<Integer, SubTask> listSubTask = new HashMap<>();
    HashMap<Integer, Status> subTaskStatus = new HashMap<>();

    public Epic(String name, String detail, Status status) {
        super(name, detail, status);
    }

    public HashMap<Integer, Status> getSubTaskStatus() {
        return subTaskStatus;
    }

    public void addSubTaskStatus(int newId, Status status) {
        subTaskStatus.put(newId, status);
    }

    public void addSubTask(int newId, SubTask newSubTask) {
        listSubTask.put(newId, newSubTask);
    }

    public HashMap<Integer, SubTask> getListSubTask() {
        return listSubTask;
    }
}


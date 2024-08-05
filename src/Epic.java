import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    /**
     * Класс эпик наследуется от Task. В нем 2 мапы:
     * 1. Хранит ID и сабтаску
     * 2. Хранит ID и статус сабтаски
     * Содержит методы для работы с мапами.
     */
   ArrayList<SubTask> listSubTask = new ArrayList<>();
    Map<Integer, Status> subTaskStatus = new HashMap<>();

    public Epic(String name, String detail, Status status) {
        super(name, detail, status);
    }

    public ArrayList<SubTask> getListSubTask() {
        return listSubTask;
    }

    public void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;

        for (Status status : subTaskStatus.values()) {
            if (status != Status.DONE) {
                allDone = false;
            } else {
                allNew = false;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}


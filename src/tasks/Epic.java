package tasks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> listSubTask = new ArrayList<>();

    public Epic(String name, String detail, LocalDate date, LocalTime time, int minute, Status status) {
        super(name, detail, date, time, minute, status);
    }

    public void addSubTask(int subTaskId) {
        listSubTask.add(subTaskId);
    }

    public void removeSubTask(int subTaskId) {
        listSubTask.remove(Integer.valueOf(subTaskId));
    }

    public List<Integer> getListSubTask() {
        return listSubTask;
    }

    public void removeAllSubtasks() {
        listSubTask.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "Название: '" + this.getName() + '\'' +
                ", детали: '" + this.getDetail() + '\'' +
                ", id :" + this.getId() +
                ", статус: " + this.getStatus() +
                '}';
    }
}


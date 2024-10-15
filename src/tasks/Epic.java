package tasks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> listSubTask = new ArrayList<>();
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String name, String detail, LocalDate date, LocalTime time, int minute, Status status) {
        super(name, detail, date, time, minute, status);
    }

    public void addSubTask(Task subTask) {
        listSubTask.add(subTask.getId());
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
    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + this.getName() + '\'' +
                ", detail='" + this.getDetail() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", duration=" + this.getDuration() +
                ", startTime=" + this.getStartTime() +
                ", endTime=" + this.getEndTime() +
                '}';
    }
}


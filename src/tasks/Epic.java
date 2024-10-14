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
        this.startTime = LocalDateTime.of(date, time);
        this.endTime = this.startTime.plusMinutes(minute);
    }

    public void addSubTask(Task subTask) {
        listSubTask.add(subTask.getId());
        addSubTaskTimes(subTask);
    }

    public void removeSubTask(Task subTask, List<Task> allSubTasks) {
        listSubTask.remove(Integer.valueOf(subTask.getId()));
        removeSubTaskTimes(subTask, allSubTasks);
    }

    public List<Integer> getListSubTask() {
        return listSubTask;
    }

    public void removeAllSubtasks() {
        listSubTask.clear();
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;
    }

    private void addSubTaskTimes(Task subTask) {
        duration = duration.plus(subTask.getDuration());
        if (startTime == null || subTask.getStartTime().isBefore(startTime)) {
            startTime = subTask.getStartTime();
        }
        LocalDateTime subTaskEndTime = subTask.getStartTime().plus(subTask.getDuration());
        if (endTime == null || subTaskEndTime.isAfter(endTime)) {
            endTime = subTaskEndTime;
        }
    }

    private void removeSubTaskTimes(Task subTask, List<Task> allSubTasks) {
        duration = duration.minus(subTask.getDuration());
        recalculateStartAndEndTime(allSubTasks);
    }

    private void recalculateStartAndEndTime(List<Task> allSubTasks) {
        startTime = null;
        endTime = null;
        for (Task subTask : allSubTasks) {
            if (listSubTask.contains(subTask.getId())) {
                if (startTime == null || subTask.getStartTime().isBefore(startTime)) {
                    startTime = subTask.getStartTime();
                }
                LocalDateTime subTaskEndTime = subTask.getStartTime().plus(subTask.getDuration());
                if (endTime == null || subTaskEndTime.isAfter(endTime)) {
                    endTime = subTaskEndTime;
                }
            }
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
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


package tasks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Task {
    /**
     * Коасс Task с основными параметрами для работы. name,detail,status,id.
     */
    private boolean initialized = false;
    private String name;
    private String detail;
    private int id;
    private Status status;

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    private Duration duration;
    private LocalDateTime startTime;
    private LocalDate date;
    private LocalTime time;

    public Task(String name, String detail, LocalDate date, LocalTime time, int minute, Status status) {
        this.name = name;
        this.detail = detail;
        this.date = date;
        this.time = time;
        this.status = status;
        this.duration = Duration.ofMinutes(minute);
        this.startTime = LocalDateTime.of(this.date, this.time);
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "Название: '" + name + '\'' +
                ", детали: '" + detail + '\'' +
                ", id :" + id +
                ", статус: " + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(detail, task.detail) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, detail, id, status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}



package tasks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Task {
    private final Duration duration;
    private final LocalDateTime startTime;
    private final LocalDate date;
    private final LocalTime time;
    /**
     * Коасс Task с основными параметрами для работы. name,detail,status,id.
     */
    private boolean initialized = false;
    private String name;
    private String detail;
    private int id;
    private Status status;


    public Task(String name, String detail,
                LocalDate date, LocalTime time, int minute, Status status) {
        this.name = name;
        this.detail = detail;
        this.date = date;
        this.time = time;
        this.status = status;
        this.duration = Duration.ofMinutes(minute);
        this.startTime = LocalDateTime.of(this.date, this.time);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", date=" + date +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(detail, task.detail) && status == task.status && Objects.equals(duration, task.duration) && Objects.equals(date, task.date) && Objects.equals(time, task.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, detail, id, status, duration, date, time);
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
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



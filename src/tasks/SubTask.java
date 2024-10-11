package tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String name, String detail, LocalDate date, LocalTime time, int minute, Status status, Integer epicId) {
        super(name, detail, date, time, minute, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + this.getName() + '\'' +
                ", detail='" + this.getDetail() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", duration=" + this.getDuration() +
                ", date=" + this.getDate() +
                ", time=" + this.getTime() +
                ", epicId: " + epicId +
                '}';
    }
}

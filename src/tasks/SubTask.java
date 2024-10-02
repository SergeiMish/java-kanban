package tasks;

public class SubTask extends Task {

    private final Integer epicId;

    public int getEpicId() {
        return epicId;
    }

    public SubTask(String name, String detail, Status status, Integer epicId) {
        super(name, detail, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "Название: '" + this.getName() + '\'' +
                ", детали: '" + this.getDetail() + '\'' +
                ", id :" + this.getId() +
                ", статус: " + this.getStatus() +
                ", epicId: " + epicId +
                '}';
    }
}

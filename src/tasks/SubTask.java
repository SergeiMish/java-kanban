package tasks;

public class SubTask extends Task {
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    private int epicId;

    public SubTask(String name, String detail, Status status, int epicID) {
        super(name, detail, status);
        this.epicId = epicID;
    }

}

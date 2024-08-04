import java.util.List;

public class Task {
    /**
     * Коасс Task с основными параметрами для работы. name,detail,status,id.
     */
    private String name;
    private String detail;
    private int id;
    private Status status;

    public Task(String name, String detail, Status status) {
        this.name = name;
        this.detail = detail;
        this.status = status;
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
    }
}



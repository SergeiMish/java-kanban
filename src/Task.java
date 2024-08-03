public class Task {
    private String name;
    private String detail;
    private int id;
    private static String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        Task.status = status;
    }

    public Task(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }
}



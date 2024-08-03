public class Task {
    private String name;
    private String detail;
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getProgress() {
        return progress;
    }

    public static void setProgress(String progress) {
        Task.progress = progress;
    }

    private static String progress;

    public Task(String name, String detail, int id) {
        this.name = name;
        this.detail = detail;
        this.id = id;
    }
}




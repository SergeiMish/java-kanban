import java.util.HashMap;

public class Epic extends Task{
    HashMap<Integer, SubTask>listSubTask = new HashMap<>();
    public Epic(String name, String detail) {
        super(name, detail);
    }

    public void addSubTask(int newId, SubTask newSubTask) {
        listSubTask.put(newId, newSubTask);
    }
    public HashMap<Integer, SubTask> getListSubTask() {
        return listSubTask;
    }
}

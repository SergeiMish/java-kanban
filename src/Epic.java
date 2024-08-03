import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task{
    public Epic(String name, String detail, int id) {
        super(name, detail);


    }

    public void addSubTask(int newId, SubTask newSubTask) {
        HashMap<Integer, SubTask>addSubTask = new HashMap<>();
    }
}

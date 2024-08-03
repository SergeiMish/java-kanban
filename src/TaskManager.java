import java.util.HashMap;

public class TaskManager {
    int id = 1;
    HashMap<Integer, Task> idToTask = new HashMap<>();
    HashMap<Integer, Epic> idToEpic = new HashMap<>();
    HashMap<Integer, SubTask> idToSubTask = new HashMap<>();

    public void listTask() {
        for (Task value : idToTask.values()) {
            System.out.println(value);
        }
    }

    public void deleteAllTask() {
        idToTask.clear();
    }

    public int findTask(Task findTask) {
        int task = findTask.getId();
        if (idToTask.containsKey(task)) {
            return task;
        } else {
            System.out.println("Такого Id не существует");
            return -1;
        }
    }

    public Task createTask(Task newTask) {
        int newId = id++;
        newTask.setId(newId);
        idToTask.put(newId, newTask);
        Status status = Status.NEW;
        return newTask;
    }
    public Task updateTask(Task updateTask) {
        int id = updateTask.getId();
        if (idToTask.containsKey(id)) {
            idToTask.put(updateTask.getId(), updateTask);
        }
        return updateTask;
    }

    public Task deleteTask(Integer id) {
        Task task = idToTask.get(id);
        idToTask.remove(id);
        return task;
    }


    public void listEpic() {
        for (Task value : idToEpic.values()) {
            System.out.println(value);
        }
    }

    public void deleteAllEpic() {
        idToEpic.clear();
    }

    public int findEpic(Task findEpic) {
        int epic = findEpic.getId();
        if (idToTask.containsKey(epic)) {
            return epic;
        } else {
            System.out.println("Такого Id не существует");
            return -1;
        }
    }

    public Epic createEpic(Epic newEpic, Status status) {
            int newId = id++;
            newEpic.setId(newId);
            idToEpic.put(newId, newEpic);
            return newEpic;
    }

    public Epic updateEpic(Epic updateEpic) {
        int id = updateEpic.getId();
        if (idToEpic.containsKey(id)) {
            idToEpic.put(updateEpic.getId(), updateEpic);
        }
        return updateEpic;
    }

    public Epic deleteEpic(Integer id) {
        Epic epic = idToEpic.get(id);
        idToEpic.remove(id);
        return epic;
    }

    public void listSubTask() {
        for (SubTask value : idToSubTask.values()) {
            System.out.println(value);
        }
    }

    public void deleteAllSubTask() {
        idToEpic.clear();
    }

    public int findSubTask(SubTask findSubTask) {
        int subTask = findSubTask.getId();
        if (idToSubTask.containsKey(subTask)) {
            return subTask;
        } else {
            System.out.println("Такого Id не существует");
            return -1;
        }
    }

    public SubTask createSubTask(SubTask newSubTask, Epic epic, Status status) {
        int newId = id++;
        newSubTask.setId(newId);
        epic.addSubTask(newId, newSubTask);
        idToSubTask.put(newId, newSubTask);
        return newSubTask;
    }

    public SubTask updateSubTask(SubTask updateSubTask) {
        int id = updateSubTask.getId();
        if (idToSubTask.containsKey(id)) {
            idToSubTask.put(updateSubTask.getId(), updateSubTask);
        }
        return updateSubTask;
    }

    public SubTask deleteSubTask(Integer id) {
        SubTask subTask = idToSubTask.get(id);
        idToSubTask.remove(id);
        return subTask;
    }
}


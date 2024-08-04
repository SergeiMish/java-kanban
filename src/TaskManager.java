import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    int id = 1;
    HashMap<Integer, Task> idToTask = new HashMap<>();
    HashMap<Integer, Epic> idToEpic = new HashMap<>();
    HashMap<Integer, SubTask> idToSubTask = new HashMap<>();

    public void listTasks() {
        for (Task task : idToTask.values()) {
            System.out.println("ID: " + task.getId() + ", Name: " + task.getName() + ", Detail: " + task.getDetail() +
                    ", Status: " + task.getStatus());
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
        newTask.setStatus(Status.NEW);
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
        for (Epic epic : idToEpic.values()) {
            System.out.println("ID: " + epic.getId() + ", Name: " + epic.getName() + ", Detail: " + epic.getDetail() +
                    ", Status: " + epic.getStatus());
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

    public Epic createEpic(Epic newEpic) {
        int newId = id++;
        newEpic.setId(newId);
        idToEpic.put(newId, newEpic);
        newEpic.setStatus(Status.NEW);
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
        for (SubTask subTask : idToSubTask.values()) {
            System.out.println("ID: " + subTask.getId() + ", Name: " + subTask.getName() + ", Detail: " + subTask.getDetail() +
                    ", Status: " + subTask.getStatus());
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

    public SubTask createSubTask(SubTask newSubTask, Epic epic) {
        int newId = id++;
        newSubTask.setId(newId);
        epic.addSubTask(newId, newSubTask);
        idToSubTask.put(newId, newSubTask);
        newSubTask.setStatus(Status.NEW);
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

    public void getAllSubTasksOfEpic(Epic epic) {
        int epicId = epic.getId();
        if (idToEpic.containsKey(epicId)){
            System.out.println(epic.getListSubTask());
        }
    }
}


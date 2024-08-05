import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int id = 1;
    /**
     * Мапы для хранения Task, Epic, SubTask
     */
    Map<Integer, Task> idToTask = new HashMap<>();
    Map<Integer, Epic> idToEpic = new HashMap<>();
    Map<Integer, SubTask> idToSubTask = new HashMap<>();

    /**
     * Метод вывода задач
     */
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>(idToTask.values());
        return taskList;
    }

    /**
     * Метод удаления всех задач
     */
    public void deleteAllTask() {
        idToTask.clear();
    }

    /**
     * Метод поиска по ID задачи
     *
     * @return
     */
    public Task findTask(Task findTask) {
        int taskId = findTask.getId();
        if (idToTask.containsKey(taskId)) {
            return idToTask.get(taskId);
        } else {
            System.out.println("Такого Id не существует");
            return null;
        }
    }

    /**
     * Метод создания задачи
     *
     * @return
     */
    public Task createTask(Task newTask) {
        int newId = id++;
        newTask.setId(newId);
        idToTask.put(newId, newTask);
        return newTask;
    }

    /**
     * Метод обновления задачи
     *
     * @return
     */
    public Task updateTask(Task updateTask) {
        int id = updateTask.getId();
        if (idToTask.containsKey(id)) {
            idToTask.put(updateTask.getId(), updateTask);
        }
        return updateTask;
    }

    /**
     * Метод удаления задачи по ID
     *
     * @return
     */
    public Task deleteTask(Integer id) {
        Task task = idToTask.get(id);
        idToTask.remove(id);
        return task;
    }

    /**
     * Вывод на экран скиска эпиков
     */
    public List<Epic> listEpic() {
        List<Epic> epicList = new ArrayList<>(idToEpic.values());
        return epicList;
    }

    /**
     * Удаление всех эпиков
     */
    public void deleteAllEpic() {
        for (Epic epic : idToEpic.values()) {
            epic.getListSubTask().clear();
        }
        idToEpic.clear();
    }

    /**
     * Метод поиска эпика по ID
     *
     * @return
     */
    public Epic findEpic(int epicId) {
        if (idToEpic.containsKey(epicId)) {
            return idToEpic.get(epicId);
        } else {
            System.out.println("Такого Id не существует");
            return null;
        }
    }

    /**
     * Метод создания эпика
     *
     * @return
     */
    public Epic createEpic(Epic newEpic) {
        int newId = id++;
        newEpic.setId(newId);
        idToEpic.put(newId, newEpic);
        if (newEpic.listSubTask == null) {
            newEpic.setStatus(Status.NEW);
        }
        return newEpic;
    }

    /**
     * Метод обновления эпика
     *
     * @return
     */
    public Epic updateEpic(Epic updateEpic) {
        int id = updateEpic.getId();
        if (idToEpic.containsKey(id)) {
            idToEpic.put(updateEpic.getId(), updateEpic);
        }
        return updateEpic;
    }

    /**
     * Метод удаления эпика по ID
     *
     * @return
     */
    public Epic deleteEpic(Integer id) {
        Epic epic = idToEpic.get(id);
        idToEpic.remove(id);
        return epic;
    }

    /**
     * Вывод на экран всех сабтасок
     */
    public List<SubTask> listSubTask() {
        List<SubTask> subTasks = new ArrayList<>(idToSubTask.values());
        return subTasks;
    }

    /**
     * Удаление всех сабтасок
     */
    public void deleteAllSubTask() {
        for (Epic epic : idToEpic.values()) {
            epic.getListSubTask().clear();
            epic.updateEpicStatus(epic);
        }
    }

    /**
     * Поиск сабтасок по ID
     *
     * @return
     */
    public SubTask findSubTask(int subtaskId) {
        if (idToSubTask.containsKey(subtaskId)) {
            return idToSubTask.get(subtaskId);
        } else {
            System.out.println("Такого Id не существует");
            return null;
        }
    }

    /**
     * Создание сабтаски, привязка к эпику, проверка статуса.
     *
     * @return
     */
    public SubTask createSubTask(SubTask newSubTask, Epic epic) {
        int newId = id++;
        newSubTask.setId(newId);
        idToSubTask.put(newId, newSubTask);
        epic.updateEpicStatus(epic);
        return newSubTask;
    }

    /**
     * Обновление сабтаски, проверка статуса, обновление статуса эпика.
     *
     * @return
     */
    public SubTask updateSubTask(SubTask updateSubTask, Epic epic) {
        int id = updateSubTask.getId();
        if (idToSubTask.containsKey(id)) {
            idToSubTask.put(updateSubTask.getId(), updateSubTask);
        }
        epic.updateEpicStatus(epic);
        return updateSubTask;
    }

    /**
     * Удаление сабтаски по ID
     *
     * @param id
     * @return
     */
    public SubTask deleteSubTask(Integer id) {
        SubTask subTask = idToSubTask.get(id);
        idToSubTask.remove(id);
        for (Epic epic : idToEpic.values()) {
            if (epic.getListSubTask().contains(subTask)) {
                epic.updateEpicStatus(epic);
            } else {
                return null;
            }
        }
        return subTask;
    }

    /**
     * Вывод на экран всех сабтасок конкретного эпика.
     */
    public List<SubTask> getAllSubTasksOfEpic(Epic epic) {
        return epic.getListSubTask();
    }
}



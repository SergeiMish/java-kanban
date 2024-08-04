import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    int id = 1;
    /**
     * Мапы для хранения Task, Epic, SubTask
     */
    HashMap<Integer, Task> idToTask = new HashMap<>();
    HashMap<Integer, Epic> idToEpic = new HashMap<>();
    HashMap<Integer, SubTask> idToSubTask = new HashMap<>();

    /**
     * Метод вывода задач
     */
    public void listTasks() {
        for (Task task : idToTask.values()) {
            System.out.println("ID: " + task.getId() + ", Name: " + task.getName() + ", Detail: " + task.getDetail() +
                    ", Status: " + task.getStatus());
        }
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
     * @param findTask
     * @return
     */
    public int findTask(Task findTask) {
        int task = findTask.getId();
        if (idToTask.containsKey(task)) {
            return task;
        } else {
            System.out.println("Такого Id не существует");
            return -1;
        }
    }

    /**
     * Метод создания задачи
     *
     * @param newTask
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
     * @param updateTask
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
     * @param id
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
    public void listEpic() {
        for (Epic epic : idToEpic.values()) {
            System.out.println("ID: " + epic.getId() + ", Name: " + epic.getName() + ", Detail: " + epic.getDetail() +
                    ", Status: " + epic.getStatus());
        }
    }

    /**
     * Удаление всех эпиков
     */
    public void deleteAllEpic() {
        idToEpic.clear();
    }

    /**
     * Метод поиска эпика по ID
     *
     * @param findEpic
     * @return
     */
    public int findEpic(Task findEpic) {
        int epic = findEpic.getId();
        if (idToTask.containsKey(epic)) {
            return epic;
        } else {
            System.out.println("Такого Id не существует");
            return -1;
        }
    }

    /**
     * Метод создания эпика
     *
     * @param newEpic
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
     * @param updateEpic
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
     * @param id
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
    public void listSubTask() {
        for (SubTask subTask : idToSubTask.values()) {
            System.out.println("ID: " + subTask.getId() + ", Name: " + subTask.getName() + ", Detail: " + subTask.getDetail() +
                    ", Status: " + subTask.getStatus());
        }
    }

    /**
     * Удаление всех сабтасок
     */
    public void deleteAllSubTask() {
        idToEpic.clear();
    }

    /**
     * Поиск сабтасок по ID
     *
     * @param findSubTask
     * @return
     */
    public int findSubTask(SubTask findSubTask) {
        int subTask = findSubTask.getId();
        if (idToSubTask.containsKey(subTask)) {
            return subTask;
        } else {
            System.out.println("Такого Id не существует");
            return -1;
        }
    }

    /**
     * Создание сабтаски, привязка к эпику, проверка статуса.
     *
     * @param newSubTask
     * @param epic
     * @return
     */
    public SubTask createSubTask(SubTask newSubTask, Epic epic) {
        int newId = id++;
        newSubTask.setId(newId);
        epic.addSubTask(newId, newSubTask);
        epic.addSubTaskStatus(newId, newSubTask.getStatus());
        idToSubTask.put(newId, newSubTask);
        for (Map.Entry<Integer, Status> entry1 : epic.getSubTaskStatus().entrySet()) {
            String status = entry1.getValue().name();
            if (status.equals(Status.NEW) || epic.listSubTask == null) {
                epic.setStatus(Status.NEW);
            }
        }
        return newSubTask;
    }

    /**
     * Обновление сабтаски, проверка статуса, обновление статуса эпика.
     *
     * @param updateSubTask
     * @param epic
     * @return
     */
    public SubTask updateSubTask(SubTask updateSubTask, Epic epic) {
        int id = updateSubTask.getId();
        if (idToSubTask.containsKey(id)) {
            idToSubTask.put(updateSubTask.getId(), updateSubTask);
        }
        for (Map.Entry<Integer, Status> entry1 : epic.getSubTaskStatus().entrySet()) {
            String status = entry1.getValue().name();
            if (status.equals(Status.DONE)) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
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
        return subTask;
    }

    /**
     * Вывод на экран всех сабтасок конкретного эпика.
     *
     * @param epic
     */
    public void getAllSubTasksOfEpic(Epic epic) {
        int epicId = epic.getId();
        if (idToEpic.containsKey(epicId)) {
            for (Map.Entry<Integer, SubTask> entry : epic.getListSubTask().entrySet()) {
                for (Map.Entry<Integer, Status> entry1 : epic.getSubTaskStatus().entrySet()) {
                    String status = entry1.getValue().name();
                    int subTaskId = entry.getKey();
                    String subTaskName = entry.getValue().getName();
                    System.out.println("Subtask ID: " + subTaskId + ", Subtask Name: " + subTaskName + ", Status " + status);
                }
            }
        }
    }
}



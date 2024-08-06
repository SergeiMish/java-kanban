package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private int id = 1;
    /**
     * Мапы для хранения Task, Epic, SubTask
     */
    private final Map<Integer, Task> idToTask = new HashMap<>();
    private final Map<Integer, Epic> idToEpic = new HashMap<>();
    private final Map<Integer, SubTask> idToSubTask = new HashMap<>();

    /**
     * Метод вывода задач
     */
    public List<Task> getListTasks() {
        return new ArrayList<>(idToTask.values());
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
    public List<Epic> getListEpic() {
        return new ArrayList<>(idToEpic.values());
    }

    /**
     * Удаление всех эпиков
     */
    public void deleteAllEpic() {
        idToSubTask.clear();
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
        if (newEpic.getListSubTask() == null) {
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
            Epic epic = idToEpic.get(id);
            epic.setName(updateEpic.getName());
            epic.setDetail(updateEpic.getDetail());
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
        for (Integer subTaskId : epic.getListSubTask()) {
            idToSubTask.remove(subTaskId);
        }
        idToEpic.remove(id);
        return epic;
    }

    /**
     * Вывод на экран всех сабтасок
     */
    public List<SubTask> getlistSubTask() {
        return new ArrayList<>(idToSubTask.values());
    }

    /**
     * Удаление всех сабтасок
     */
    public void deleteAllSubTask() {
        for (Epic epic : idToEpic.values()) {
            epic.removeAllSubtasks();
            epic.updateEpicStatus();
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
        newSubTask.setEpicId(newId);
        idToSubTask.put(newId, newSubTask);
        epic.addSubTask(newSubTask);
        epic.updateEpicStatus();
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
        epic.updateEpicStatus();
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
        Epic epic = idToEpic.get(subTask.getEpicId());
        if (epic != null) {
            epic.removeSubTask(id);
            epic.updateEpicStatus();
        }
        return subTask;
    }


    /**
     * Вывод на экран всех сабтасок конкретного эпика.
     */
    public List<Integer> getAllSubTasksOfEpic(Epic epic) {
        return epic.getListSubTask();
    }
}



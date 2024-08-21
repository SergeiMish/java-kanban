package manager;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;

    private final Map<Integer, Task> idToTask = new HashMap<>();
    private final Map<Integer, Epic> idToEpic = new HashMap<>();
    private final Map<Integer, SubTask> idToSubTask = new HashMap<>();
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getListTasks() {
        Task task = idToTask.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return new ArrayList<>(idToTask.values());
    }

    @Override
    public void deleteAllTask() {
        idToTask.clear();
    }

    @Override
    public Task findTask(int taskId) {
        if (idToTask.containsKey(taskId)) {
            return idToTask.get(taskId);
        } else {
            System.out.println("Такого Id не существует");
            return null;
        }
    }

    @Override
    public Task createTask(Task newTask) {
        int newId = id++;
        newTask.setId(newId);
        idToTask.put(newId, newTask);
        return newTask;
    }

    @Override
    public Task updateTask(Task updateTask) {
        int id = updateTask.getId();
        if (idToTask.containsKey(id)) {
            idToTask.put(updateTask.getId(), updateTask);
        }
        return updateTask;
    }

    @Override
    public Task deleteTask(Integer id) {
        Task task = idToTask.get(id);
        idToTask.remove(id);
        return task;
    }

    @Override
    public List<Epic> getListEpic() {
        Epic epic = idToEpic.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return new ArrayList<>(idToEpic.values());
    }


    @Override
    public void deleteAllEpic() {
        idToSubTask.clear();
        idToEpic.clear();
    }

    @Override
    public Epic findEpic(int epicId) {
        if (idToEpic.containsKey(epicId)) {
            return idToEpic.get(epicId);
        } else {
            System.out.println("Такого Id не существует");
            return null;
        }
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        int newId = id++;
        newEpic.setId(newId);
        idToEpic.put(newId, newEpic);
        if (newEpic.getListSubTask() == null) {
            newEpic.setStatus(Status.NEW);
        }
        return newEpic;
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = idToEpic.get(epicId);
        List<Integer> listSubTask = epic.getListSubTask();
        if (listSubTask.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subTaskId : listSubTask) {
            SubTask subTask = idToSubTask.get(subTaskId);
            if (subTask == null) continue;

            if (subTask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subTask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public Epic updateEpic(Epic updateEpic) {
        int id = updateEpic.getId();
        if (idToEpic.containsKey(id)) {
            Epic epic = idToEpic.get(id);
            epic.setName(updateEpic.getName());
            epic.setDetail(updateEpic.getDetail());
        }
        return updateEpic;
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic epic = idToEpic.get(id);
        for (Integer subTaskId : epic.getListSubTask()) {
            idToSubTask.remove(subTaskId);
        }
        idToEpic.remove(id);
        return epic;
    }

    @Override
    public List<SubTask> getSubTasks() {
        SubTask subTask = idToSubTask.get(id);
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return new ArrayList<>(idToSubTask.values());
    }

    @Override
    public void deleteAllSubTask() {
        for (Epic epic : idToEpic.values()) {
            epic.removeAllSubtasks();
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public SubTask findSubTask(int subtaskId) {
        if (idToSubTask.containsKey(subtaskId)) {
            return idToSubTask.get(subtaskId);
        } else {
            System.out.println("Такого Id не существует");
            return null;
        }
    }

    @Override
    public SubTask createSubTask(SubTask newSubTask) {
        Epic epic = idToEpic.get(newSubTask.getEpicId());
        if (epic != null) {
            int newId = id++;
            newSubTask.setId(newId);
            idToSubTask.put(newId, newSubTask);
            epic.addSubTask(newId);
            updateEpicStatus(epic.getId());

            return newSubTask;
        }
        return null;
    }

    public SubTask updateSubTask(SubTask updateSubTask, Epic epic) {
        int id = updateSubTask.getId();
        if (idToSubTask.containsKey(id)) {
            idToSubTask.put(updateSubTask.getId(), updateSubTask);
        }
        updateEpicStatus(epic.getId());
        return updateSubTask;
    }

    @Override
    public SubTask deleteSubTask(Integer id) {
        SubTask subTask = idToSubTask.get(id);
        idToSubTask.remove(id);
        Epic epic = idToEpic.get(subTask.getEpicId());
        if (epic != null) {
            epic.removeSubTask(id);
            updateEpicStatus(epic.getId());
        }
        return subTask;
    }

    @Override
    public List<Integer> getListSubTasksOfEpic(int epicId) {
        return idToEpic.containsKey(epicId) ? idToEpic.get(epicId).getListSubTask() : null;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}


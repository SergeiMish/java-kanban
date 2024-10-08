package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;

    private final Map<Integer, Task> idToTask = new HashMap<>();
    private final Map<Integer, Epic> idToEpic = new HashMap<>();
    private final Map<Integer, SubTask> idToSubTask = new HashMap<>();
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getListTasks() {
        return new ArrayList<>(idToTask.values());
    }

    @Override
    public void deleteAllTask() {
        idToTask.clear();
    }

    @Override
    public Task getTaskId(int taskId) {
        Task task = idToTask.get(taskId);
        if (task != null) {
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Task createTask(Task newTask) {
        int newId = id++;
        if (!newTask.isInitialized()) {
            newTask.setId(newId);
        }
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
    public Task deleteTask(Integer taskId) {
        Task task = idToTask.get(taskId);
        idToTask.remove(id);
        return task;
    }

    @Override
    public List<Epic> getListEpic() {
        return new ArrayList<>(idToEpic.values());
    }

    @Override
    public void deleteAllEpic() {
        idToSubTask.clear();
        idToEpic.clear();
    }

    @Override
    public Epic getEpicId(int epicId) {
        Epic epic = idToEpic.get(epicId);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        int newId = id++;
        if (!newEpic.isInitialized()) {
            newEpic.setId(newId);
        } else {
            newId = newEpic.getId();
        }
        idToEpic.put(newId, newEpic);
        if (newEpic.getListSubTask() == null) {
            newEpic.setStatus(Status.NEW);
        }
        return newEpic;
    }

    public LocalDateTime getEndTimeEpic(int epicId) {
        Epic epic = idToEpic.get(epicId);
        if (epic == null) {
            return null;
        }

        List<Integer> listSubTask = epic.getListSubTask();
        LocalDateTime EndTime = null;

        for (Integer subTaskId : listSubTask) {
            SubTask subTask = idToSubTask.get(subTaskId);
            if (subTask == null) continue;

            LocalDateTime subTaskEndTime = subTask.getStartTime().plus(subTask.getDuration());
            if (EndTime == null || subTaskEndTime.isAfter(EndTime)) {
                EndTime = subTaskEndTime;
            }
        }

        return EndTime;
    }

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
    public List<SubTask> getListSubTask() {
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
    public SubTask getSubTaskId(Integer subtaskId) {
        SubTask subTask = idToSubTask.get(subtaskId);
        if (subTask != null) {
            historyManager.add(subTask);
            return subTask;
        }
        return null;
    }

    @Override
    public SubTask createSubTask(SubTask newSubTask) {
        Epic epic = idToEpic.get(newSubTask.getEpicId());
        if (epic != null) {
            if (!newSubTask.isInitialized()) {
                int newId = id++;
                newSubTask.setId(newId);
                idToSubTask.put(newId, newSubTask);
                epic.addSubTask(newId);
                updateEpicStatus(epic.getId());

                return newSubTask;
            } else {
                idToSubTask.put(newSubTask.getId(), newSubTask);
                updateEpicStatus(epic.getId());
            }
            return newSubTask;
        }
        return null;
    }

    public SubTask updateSubTask(SubTask updateSubTask) {
        int newId = updateSubTask.getId();
        if (idToSubTask.containsKey(newId)) {
            SubTask currentSubtaskId = idToSubTask.get(newId);
            if (currentSubtaskId.getEpicId() == (updateSubTask.getEpicId())) {
                idToSubTask.put(newId, updateSubTask);
                Epic epic = idToEpic.get(updateSubTask.getEpicId());
                if (epic != null) {
                    updateEpicStatus(epic.getId());
                }
                return updateSubTask;
            }
        }
        return null;
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
    public List<SubTask> getListSubTasksOfEpic(int epicId) {
        Epic epic = idToEpic.get(epicId);
        if (epic != null) {
            List<SubTask> subTasks = new ArrayList<>();
            for (int subTaskId : epic.getListSubTask()) {
                SubTask subTask = idToSubTask.get(subTaskId);
                if (subTask != null) {
                    subTasks.add(subTask);
                }
            }
            return subTasks;
        }
        return null;
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}


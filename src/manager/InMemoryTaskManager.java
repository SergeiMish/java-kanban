package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> idToTask = new HashMap<>();
    private final Map<Integer, Epic> idToEpic = new HashMap<>();
    private final Map<Integer, SubTask> idToSubTask = new HashMap<>();
    private final HistoryManager historyManager;
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(new TaskStartTimeComparator());
    private int id = 1;

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

    private void updatePrioritizedTasks() {
        prioritizedTasks.clear();
        for (Task task : idToTask.values()) {
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
        for (Epic epic : idToEpic.values()) {
            if (epic.getStartTime() != null) {
                prioritizedTasks.add(epic);
            }
        }
        for (SubTask subTask : idToSubTask.values()) {
            if (subTask.getStartTime() != null) {
                prioritizedTasks.add(subTask);
            }
        }
    }

    public List<Task> getPrioritizedTasks() {
        updatePrioritizedTasks();
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean isOverlapping(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public boolean canAddTask(Task newTask) {
        return getPrioritizedTasks().stream()
                .noneMatch(existingTask -> isOverlapping(existingTask, newTask));
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
        if (canAddTask(newTask)) {
            int newId = id++;
            if (!newTask.isInitialized()) {
                newTask.setId(newId);
            }
            idToTask.put(newId, newTask);
            updatePrioritizedTasks();
            return newTask;
        } else {
            throw new IllegalArgumentException("Задачи пересекаются по времени");
        }
    }

    @Override
    public Task updateTask(Task updateTask) {
        int id = updateTask.getId();
        if (idToTask.containsKey(id)) {
            idToTask.put(updateTask.getId(), updateTask);
            updatePrioritizedTasks();
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
            updatePrioritizedTasks();
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
        if (idToEpic.isEmpty()) {
            return null;
        }
        Epic epic = idToEpic.get(epicId);
        if (epic == null) {
            return null;
        }
        List<Integer> listSubTask = epic.getListSubTask();
        LocalDateTime endTime = null;

        for (Integer subTaskId : listSubTask) {
            SubTask subTask = idToSubTask.get(subTaskId);
            if (subTask == null) continue;

            LocalDateTime subTaskEndTime = subTask.getStartTime().plus(subTask.getDuration());
            if (endTime == null || subTaskEndTime.isAfter(endTime)) {
                endTime = subTaskEndTime;
            }
        }
        return endTime;
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
            updatePrioritizedTasks();
        }
        return updateEpic;
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic epic = idToEpic.get(id);
        epic.getListSubTask().forEach(idToSubTask::remove);
        return epic;
    }

    @Override
    public List<SubTask> getListSubTask() {
        return new ArrayList<>(idToSubTask.values());
    }

    @Override
    public void deleteAllSubTask() {
        idToEpic.values().forEach(epic -> {
            epic.removeAllSubtasks();
            updateEpicStatus(epic.getId());
        });
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
        if (canAddTask(newSubTask)) {
            Epic epic = idToEpic.get(newSubTask.getEpicId());
            if (epic != null) {
                if (!newSubTask.isInitialized()) {
                    int newId = id++;
                    newSubTask.setId(newId);
                    idToSubTask.put(newId, newSubTask);
                    epic.addSubTask(newId);
                    updateEpicStatus(epic.getId());
                    updatePrioritizedTasks();
                    return newSubTask;
                } else {
                    idToSubTask.put(newSubTask.getId(), newSubTask);
                    updateEpicStatus(epic.getId());
                }
                return newSubTask;
            }
        } else {
            throw new IllegalArgumentException("Подзадачи пересекаются по времени");
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
                updatePrioritizedTasks();
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
            return epic.getListSubTask().stream()
                    .map(idToSubTask::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}


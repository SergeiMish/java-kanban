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

    private void addTaskToPrioritized(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void removeTaskFromPrioritized(Task task) {
        prioritizedTasks.remove(task);
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean isOverlapping(Task task1, Task task2) {
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
            addTaskToPrioritized(newTask);
            return newTask;
        } else {
            throw new IllegalArgumentException("Задачи пересекаются по времени");
        }
    }

    @Override
    public Task updateTask(Task updatedTask) {
        int taskId = updatedTask.getId();
        Task existingTask = idToTask.get(taskId);

        if (existingTask != null) {
            removeTaskFromPrioritized(existingTask);
            if (canAddTask(updatedTask)) {
                idToTask.put(taskId, updatedTask);
                addTaskToPrioritized(updatedTask);
            } else {
                addTaskToPrioritized(existingTask);
                throw new IllegalArgumentException("Обновленная задача пересекается по времени с существующими задачами");
            }
        }
        return updatedTask;
    }

    @Override
    public Task deleteTask(Integer taskId) {
        Task task = idToTask.remove(taskId); // Удаление задачи из коллекции
        if (task != null) {
            removeTaskFromPrioritized(task);
        }
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
        addTaskToPrioritized(newEpic);
        if (newEpic.getListSubTask() == null) {
            newEpic.setStatus(Status.NEW);
        }
        return newEpic;
    }

    private LocalDateTime getEndTimeEpic(int epicId) {
        Epic epic = idToEpic.get(epicId);
        if (epic == null) {
            return null;
        }
        return epic.getListSubTask()
                .stream()
                .map(idToSubTask::get)
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    private void updateEpicStatus(int epicId) {
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
        Epic epic = idToEpic.remove(id);
        if (epic != null) {
            epic.getListSubTask().forEach(idToSubTask::remove);
            removeTaskFromPrioritized(epic);
        }
        return epic;
    }

    @Override
    public List<SubTask> getListSubTask() {
        return new ArrayList<>(idToSubTask.values());
    }

    @Override
    public void deleteAllSubTask() {
        idToSubTask.clear();
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
        if (!canAddTask(newSubTask)) {
            throw new IllegalArgumentException("Подзадачи пересекаются по времени");
        }
        int epicId = newSubTask.getEpicId();
        Epic epic = idToEpic.get(epicId);
        if (epic == null) {
            throw new IllegalArgumentException("Invalid epicId " + epicId);
        }
        if (!newSubTask.isInitialized()) {
            int newId = id++;
            newSubTask.setId(newId);
            epic.addSubTask(newSubTask);
        }
        idToSubTask.put(newSubTask.getId(), newSubTask);
        addTaskToPrioritized(newSubTask);
        updateEpicStatus(epic.getId());
        return newSubTask;
    }

    public SubTask updateSubTask(SubTask updatedSubTask) {
        int subTaskId = updatedSubTask.getId();
        SubTask existingSubTask = idToSubTask.get(subTaskId);

        if (existingSubTask != null) {

            removeTaskFromPrioritized(existingSubTask);

            if (canAddTask(updatedSubTask)) {
                idToSubTask.put(subTaskId, updatedSubTask);
                addTaskToPrioritized(updatedSubTask);

                Epic epic = idToEpic.get(updatedSubTask.getEpicId());
                if (epic != null) {
                    epic.removeSubTask(existingSubTask.getId());
                    epic.addSubTask(updatedSubTask);
                    updateEpicStatus(epic.getId());
                    updateEpicTimes(epic);
                }
            } else {
                addTaskToPrioritized(existingSubTask);
                throw new IllegalArgumentException("Обновленная подзадача пересекается по времени с существующими задачами");
            }
        }
        return updatedSubTask;
    }

    @Override
    public SubTask deleteSubTask(Integer id) {
        SubTask subTask = idToSubTask.get(id);
        if (subTask != null) {
            idToSubTask.remove(id);
            Epic epic = idToEpic.get(subTask.getEpicId());
            if (epic != null) {
                epic.removeSubTask(id);
                updateEpicTimes(epic);
                updateEpicStatus(epic.getId());
                removeTaskFromPrioritized(subTask);
            }
        }
        return subTask;
    }

    private void updateEpicTimes(Epic epic) {
        Duration totalDuration = Duration.ZERO;
        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;

        for (Integer subTaskId : epic.getListSubTask()) {
            SubTask subTask = idToSubTask.get(subTaskId);
            if (subTask != null) {
                totalDuration = totalDuration.plus(subTask.getDuration());
                LocalDateTime subTaskStart = subTask.getStartTime();
                LocalDateTime subTaskEnd = subTaskStart.plus(subTask.getDuration());

                if (earliestStart == null || subTaskStart.isBefore(earliestStart)) {
                    earliestStart = subTaskStart;
                }
                if (latestEnd == null || subTaskEnd.isAfter(latestEnd)) {
                    latestEnd = subTaskEnd;
                }
            }
        }

        epic.setDuration(totalDuration);
        epic.setStartTime(earliestStart);
        epic.setEndTime(latestEnd);
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


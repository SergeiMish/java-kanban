package Interfaces;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getListTasks();

    void deleteAllTask();

    Task getTaskId(int taskId);

    Task createTask(Task newTask);

    Task updateTask(Task updateTask);

    Task deleteTask(Integer id);

    List<Epic> getListEpic();

    void deleteAllEpic();

    Epic getEpicId(int epicId);

    Epic createEpic(Epic newEpic);

    void updateEpicStatus(int epicId);

    Epic updateEpic(Epic updateEpic);

    Epic deleteEpic(Integer id);

    List<SubTask> getListSubTask();

    void deleteAllSubTask();

    SubTask getSubTaskId(int subtaskId);

    SubTask createSubTask(SubTask newSubTask);

    SubTask deleteSubTask(Integer id);

    List<Integer> getListSubTasksOfEpic(int epicId);

    List<Task> getHistory();
}

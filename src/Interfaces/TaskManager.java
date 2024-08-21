package Interfaces;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getListTasks();

    void deleteAllTask();

    Task findTask(int taskId);

    Task createTask(Task newTask);

    Task updateTask(Task updateTask);

    Task deleteTask(Integer id);

    List<Epic> getListEpic();

    void deleteAllEpic();

    Epic findEpic(int epicId);

    Epic createEpic(Epic newEpic);

    void updateEpicStatus(int epicId);

    Epic updateEpic(Epic updateEpic);

    Epic deleteEpic(Integer id);

    List<SubTask> getSubTasks();

    void deleteAllSubTask();

    SubTask findSubTask(int subtaskId);

    SubTask createSubTask(SubTask newSubTask);

    SubTask deleteSubTask(Integer id);

    List<Integer> getListSubTasksOfEpic(int epicId);

    List<Task> getHistory();
}

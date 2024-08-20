package Interfaces;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    List<tasks.Task> getListTasks();

    void deleteAllTask();

    tasks.Task findTask(tasks.Task findTask);

    tasks.Task createTask(tasks.Task newTask);

    tasks.Task updateTask(tasks.Task updateTask);

    tasks.Task deleteTask(Integer id);

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

    SubTask createSubTask(SubTask newSubTask, Epic epic);

    SubTask updateSubTask(SubTask updateSubTask, Epic epic);

    SubTask deleteSubTask(Integer id);

    List<Integer> getAllSubTasksOfEpic(int epicId);

    List<Task> getHistory();
}

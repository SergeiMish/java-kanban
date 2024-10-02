package interfaces;

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

    Epic updateEpic(Epic updateEpic);

    Epic deleteEpic(Integer id);

    List<SubTask> getListSubTask();

    void deleteAllSubTask();

    SubTask getSubTaskId(Integer subtaskId);

    SubTask createSubTask(SubTask newSubTask);

    SubTask updateSubTask(SubTask subTask);

    SubTask deleteSubTask(Integer id);

    List<SubTask> getListSubTasksOfEpic(int epicId);

    List<Task> getHistory();
}

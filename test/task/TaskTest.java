package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

public class TaskTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    LocalDate date1 = LocalDate.of(2024, 10, 23);
    LocalTime time1 = LocalTime.of(15, 30);
    LocalDate date2 = LocalDate.of(2024, 11, 23);
    LocalTime time2 = LocalTime.of(15, 30);


    @Test
    void createNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", date, time, 30, Status.NEW);
        final Task createdTask = taskManager.createTask(task);

        final Task savedTask = taskManager.getTaskId(createdTask.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getListTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void testTasksEquals() {
        Task task1 = new Task("Имя1", "детали", date, time, 30, NEW);
        Task task2 = new Task("Имя2", "детали", date, time, 30, NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "Задачи имеют одинаковый ID");
    }

    @Test
    void testSubTaskEquals() {
        SubTask subTask1 = new SubTask("Имя1", "детали", date, time, 30, NEW, 3);
        SubTask subTask2 = new SubTask("Имя2", "детали", date, time, 30, NEW, 3);
        subTask1.setId(1);
        subTask2.setId(1);
        assertEquals(subTask1.getId(), subTask2.getId(), "Сабтаски имеют одинаковый ID");
    }

    @Test
    void testEpicEquals() {
        Epic epic1 = new Epic("Имя1", "детали", date, time, 30, NEW);
        Epic epic2 = new Epic("Имя2", "детали", date, time, 30, NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "Эпики имеют одинаковый ID");
    }

    @Test
    void testTaskIdConflict() {
        Task task1 = new Task("Имя1", "детали", date, time, 30, NEW);
        Task task2 = new Task("Имя2", "детали", date1, time1, 30, NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(task1, taskManager.getListTasks().get(0));
        assertEquals(task2, taskManager.getListTasks().get(1));
    }

    @Test
    void allSubtasksNew() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        SubTask subTask = new SubTask("SubTask", "SubTask Detail", date1, time1, 30, Status.NEW, 1);
        SubTask subTask1 = new SubTask("SubTask 2", "SubTask Detail", date2, time2, 30, Status.NEW, 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask1);

        assertEquals(NEW, epic.getStatus(), "Статус должен быть NEW");
    }

    @Test
    void allSubtasksDone() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        SubTask subTask = new SubTask("SubTask", "SubTask Detail", date1, time1, 30, Status.DONE, 1);
        SubTask subTask1 = new SubTask("SubTask 2", "SubTask Detail", date2, time2, 30, Status.DONE, 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask1);

        assertEquals(DONE, epic.getStatus(), "Статус должен быть DONE");
    }

    @Test
    void subtasksNewAndDone() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        SubTask subTask = new SubTask("SubTask", "SubTask Detail", date1, time1, 30, NEW, 1);
        SubTask subTask1 = new SubTask("SubTask 2", "SubTask Detail", date2, time2, 30, Status.DONE, 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask1);

        assertEquals(IN_PROGRESS, epic.getStatus(), "Статус должен быть IN_PROGRESS");
    }

    @Test
    void allSubtasksInProgress() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        SubTask subTask = new SubTask("SubTask", "SubTask Detail", date1, time1, 30, IN_PROGRESS, 1);
        SubTask subTask1 = new SubTask("SubTask 2", "SubTask Detail", date2, time2, 30, IN_PROGRESS, 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask1);

        assertEquals(IN_PROGRESS, epic.getStatus(), "Статус должен быть IN_PROGRESS");
    }

    @Test
    void testEpicAndSubtaskRelation() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask", "SubTask Detail", date1, time1, 30, IN_PROGRESS, 1);
        taskManager.createSubTask(subTask);
        assertEquals(epic.getId(), subTask.getEpicId());
    }

    @Test
    void testIntervalOverlap() {
        Task task1 = new Task("Task 1", "Detail", LocalDate.now(), LocalTime.of(10, 0), 60, Status.NEW);
        Task task2 = new Task("Task 2", "Detail", LocalDate.now(), LocalTime.of(11, 30), 60, Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
    }
}

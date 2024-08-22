package tests;

import Interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tasks.Status.NEW;

public class TaskTest {
    /**
     * Почитал в пачке про условиея: Добавить тест
     * проверьте, что объект Subtask нельзя сделать своим же эпиком;
     * Куратор пишет что данный тест не реализовать и ссылаться на него.
     */
    TaskManager taskManager = Managers.getDefault();

    @Test
    void createNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
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
        Task task1 = new Task("Имя1", "детали", NEW);
        Task task2 = new Task("Имя2", "детали", NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "Задачи имеют одинаковый ID");
    }

    @Test
    void testSubTaskEquals() {
        SubTask subTask1 = new SubTask("Имя1", "детали", NEW, 3);
        SubTask subTask2 = new SubTask("Имя2", "детали", NEW, 3);
        subTask1.setId(1);
        subTask2.setId(1);
        assertEquals(subTask1.getId(), subTask2.getId(), "Сабтаски имеют одинаковый ID");
    }

    @Test
    void testEpicEquals() {
        Epic epic1 = new Epic("Имя1", "детали", NEW);
        Epic epic2 = new Epic("Имя2", "детали", NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "Эпики имеют одинаковый ID");
    }

    @Test
    void testTaskIdConflict() {
        Task task1 = new Task("Имя1", "детали", NEW);
        Task task2 = new Task("Имя2", "детали", NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(task1, taskManager.getListTasks().get(0));
        assertEquals(task2, taskManager.getListTasks().get(1));
    }
}

package tests;

import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    InMemoryTaskManager taskManager = getClass(InMemoryTaskManager);
    void CreateNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.addNewTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    /**
     * Почитал в пачке про условиея: Добавить тест
     * проверьте, что объект Subtask нельзя сделать своим же эпиком;
     * Куратор пишет что данный тест не реализовать и ссылаться на него.
     */
    @Test
    void testTasksEquals() {
        Task task1 = new Task("Имя1", "детали", Status.NEW);
        Task task2 = new Task("Имя2", "детали", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1.getId(), task2.getId(), "Задачи имеют одинаковый ID");
    }

    @Test
    void testSubTaskEquals() {
        SubTask subTask1 = new SubTask("Имя1", "детали", Status.NEW, 3);
        SubTask subTask2 = new SubTask("Имя2", "детали", Status.NEW, 3);
        subTask1.setId(1);
        subTask2.setId(1);
        assertEquals(subTask1.getId(), subTask2.getId(), "Сабтаски имеют одинаковый ID");
    }

    @Test
    void testEpicEquals() {
        Epic epic1 = new Epic("Имя1", "детали", Status.NEW);
        Epic epic2 = new Epic("Имя2", "детали", Status.NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1.getId(), epic2.getId(), "Эпики имеют одинаковый ID");
    }

    @Test
    void testTaskIdConflict() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        InMemoryTaskManager manager = new InMemoryTaskManager(historyManager);
        Task task1 = new Task("Имя1", "детали", Status.NEW);
        Task task2 = new Task("Имя2", "детали", Status.NEW);
        manager.createTask(task1);
        manager.createTask(task2);
        assertEquals(task1, manager.getListTasks().get(0));
        assertEquals(task2, manager.getListTasks().get(1));
    }
}

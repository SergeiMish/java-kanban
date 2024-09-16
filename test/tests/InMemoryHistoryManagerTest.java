package tests;

import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addHistoryTask() {
        Task task1 = new Task("Имя", "Детали", Status.NEW);
        Task task2 = new Task("Имя 2", "Детали 2", Status.NEW);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.getTaskId(task1.getId());
        taskManager.getTaskId(task2.getId());

        List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История не возвращается.");
        assertEquals(2, history.size(), "Неверное количество задач в истории.");
        assertEquals(task1, history.get(0), "Первая задача в истории не совпадает.");
        assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает.");
    }

    @Test
    void testHistoryPreservesPreviousVersionTask() {
        Task task = new Task("Имя", "Детали", Status.NEW);
        Task taskUpdate = new Task("Имя2", "Детали", Status.NEW);

        taskManager.createTask(task);
        taskManager.getTaskId(task.getId());

        taskUpdate.setId(task.getId());
        taskManager.updateTask(taskUpdate);
        taskManager.getTaskId(taskUpdate.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "История должна содержать только одну задачу.");
        assertEquals(taskUpdate, history.get(0), "История должна содержать последнюю версию задачи.");
    }
}
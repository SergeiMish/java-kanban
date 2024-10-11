package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    TaskManager taskManager = new InMemoryTaskManager();
    HistoryManager historyManager = new InMemoryHistoryManager();
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    LocalDate date1 = LocalDate.of(2024, 10, 23);
    LocalTime time1 = LocalTime.of(15, 30);
    LocalDate date2 = LocalDate.of(2024, 11, 23);
    LocalTime time2 = LocalTime.of(12, 30);

    @Test
    void addHistoryTask() {
        Task task1 = new Task("Имя", "Детали", date, time, 30, Status.NEW);
        Task task2 = new Task("Имя 2", "Детали 2", date1, time1, 30, Status.NEW);

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
        Task task = new Task("Имя", "Детали", date, time, 30, Status.NEW);
        Task taskUpdate = new Task("Имя2", "Детали", date, time, 30, Status.NEW);

        taskManager.createTask(task);
        taskManager.getTaskId(task.getId());

        taskUpdate.setId(task.getId());
        taskManager.updateTask(taskUpdate);
        taskManager.getTaskId(taskUpdate.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "История должна содержать только одну задачу.");
        assertEquals(taskUpdate, history.get(0), "История должна содержать последнюю версию задачи.");
    }

    @Test
    void emptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void duplicateHistory() {
        Task task = new Task("Task", "Detail", LocalDate.now(), LocalTime.now(), 30, Status.NEW);
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void removeFromHistory() {
        Task task1 = new Task("Task 1", "Detail", date, time, 30, Status.NEW);
        Task task2 = new Task("Task 2", "Detail", date1, time1, 30, Status.NEW);
        Task task3 = new Task("Task 3", "Detail", date2, time2, 30, Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());
        assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(task2.getId());
        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(task3.getId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}
package tests;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    @Test
    public void testTasksEquals() {
        Task task1 = new Task("Имя1", "детали", Status.NEW);
        Task task2 = new Task("Имя2", "детали", Status.NEW);
        int t1 = task1.getId();
        int t2 = task2.getId();
        assertEquals(t1, t2, "Задачи имеют одинаковый ID");
    }

    @Test
    public void testSubTaskEquals() {
        SubTask subTask1 = new SubTask("Имя1", "детали", Status.NEW);
        SubTask subTask2 = new SubTask("Имя2", "детали", Status.NEW);
        int s1 = subTask1.getId();
        int s2 = subTask2.getId();
        assertEquals(s1, s2, "Сабтаски имеют одинаковый ID");
    }

    @Test
    public void testEpicEquals() {
        Epic epic1 = new Epic("Имя1", "детали", Status.NEW);
        Epic epic2 = new Epic("Имя2", "детали", Status.NEW);
        int e1 = epic1.getId();
        int e2 = epic2.getId();
        assertEquals(e1, e2, "Эпики имеют одинаковый ID");
    }
}

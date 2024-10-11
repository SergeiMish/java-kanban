package manager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();
    LocalDate date1 = LocalDate.of(2024, 10, 23);
    LocalTime time1 = LocalTime.of(15, 30);
    LocalDate date2 = LocalDate.of(2024, 11, 23);
    LocalTime time2 = LocalTime.of(15, 30);
    LocalDate date3 = LocalDate.of(2024, 12, 23);
    LocalTime time3 = LocalTime.of(15, 30);

    @Override
    public void setUp() {
    }

    @Test
    void loadFromEmptyFile() {
        try {
            File tempFile = File.createTempFile("emptyData", null);

            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);

            assertTrue(manager.getListTasks().isEmpty(), "Список задач должен быть пустым");
            assertTrue(manager.getListEpic().isEmpty(), "Список эпиков должен быть пустым");
            assertTrue(manager.getListSubTask().isEmpty(), "Список подзадач должен быть пустым");
        } catch (IOException e) {
            fail("Не удалось создать временный файл: " + e.getMessage());
        }
    }

    @Test
    void saveMultipleTasks() {
        try {
            File tempFile = File.createTempFile("multipleTasks", null);
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

            Task task1 = new Task("Task 1", "Detail 1", date, time, 30, Status.NEW);
            Task task2 = new Task("Task 2", "Detail 2", date1, time1, 30, Status.IN_PROGRESS);
            Epic epic = new Epic("Epic 1", "Epic Detail", date2, time2, 30, Status.NEW);
            SubTask subTask = new SubTask("SubTask 1", "SubTask Detail", date3, time3, 30, Status.NEW, 3);

            manager.createTask(task1);
            manager.createTask(task2);
            manager.createEpic(epic);
            manager.createSubTask(subTask);

            List<String> lines = Files.readAllLines(tempFile.toPath());
            assertEquals(4, lines.size(), "Должно быть сохранено 4 строки");
        } catch (IOException e) {
            fail("Не удалось создать временный файл: " + e.getMessage());
        }
    }

    @Test
    void loadMultipleTasks() {
        try {
            File tempFile = File.createTempFile("loadTasks", null);
            Files.write(tempFile.toPath(), List.of(
                    "Task{name='Task1', detail='Detail1', id=1, status=NEW, duration=PT30M, date=2024-10-23, time=15:30}",
                    "Task{name='Task2', detail='Detail2', id=2, status=NEW, duration=PT30M, date=2024-10-11, time=15:38}",
                    "Epic{name='Epic', detail='Detail', id=3, status=NEW, duration=PT30M, date=2024-10-11, time=15:38}",
                    "SubTask{name='SubTask', detail='Detail', id=4, status=NEW, duration=PT40M, date=2024-11-23, time=15:30, epicId: 3}"
            ));

            FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);

            assertEquals(2, manager.getListTasks().size(), "Должно быть загружено 2 задачи");
            assertEquals(1, manager.getListEpic().size(), "Должен быть загружен 1 эпик");
            assertEquals(1, manager.getListSubTask().size(), "Должна быть загружена 1 подзадача");
        } catch (IOException e) {
            fail("Не удалось создать временный файл: " + e.getMessage());
        }
    }

    @Test
    void loadIdTask() throws IOException {
        File tempFile = File.createTempFile("loadTasks", null);
        Files.write(tempFile.toPath(), List.of(
                "Task{name='Task1', detail='Detail1', id=3, status=NEW, duration=PT30M, date=2024-10-23, time=15:30}",
                "Task{name='Task2', detail='Detail2', id=5, status=NEW, duration=PT30M, date=2024-10-11, time=15:38}",
                "Epic{name='Epic', detail='Detail', id=7, status=NEW, duration=PT30M, date=2024-10-11, time=15:38}",
                "SubTask{name='SubTask', detail='Detail', id=4, status=NEW, duration=PT40M, date=2024-11-23, time=15:30, epicId: 7}"
        ));

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);

        var listTasks = manager.getListTasks();
        var listEpics = manager.getListEpic();
        assertEquals(2, listTasks.size(), "Должно быть загружено 2 задачи");
        assertEquals(3, listTasks.get(0).getId(), "Неправильный id, должен быть id = 3");
        assertEquals(7, listEpics.get(0).getId(), "Неправильный id, должен быть id = 7");
    }
}
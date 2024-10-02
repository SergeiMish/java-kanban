package manager;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {


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

            Task task1 = new Task("Task 1", "Detail 1", Status.NEW);
            Task task2 = new Task("Task 2", "Detail 2", Status.IN_PROGRESS);
            Epic epic = new Epic("Epic 1", "Epic Detail", Status.NEW);
            SubTask subTask = new SubTask("SubTask 1", "SubTask Detail", Status.NEW, 3);

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
                    "Task{Название: 'Task 1', детали: 'Detail 1', id :1, статус: NEW}",
                    "Task{Название: 'Task 2', детали: 'Detail 2', id :2, статус: IN_PROGRESS}",
                    "Epic{Название: 'Epic 1', детали: 'Epic Detail', id :3, статус: NEW}",
                    "SubTask{Название: 'SubTask 1', детали: 'SubTask Detail', id :4, статус: NEW, epicId: 3}"
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
                "Task{Название: 'Task 1', детали: 'Detail 1', id :3, статус: NEW}",
                "Task{Название: 'Task 2', детали: 'Detail 2', id :5, статус: IN_PROGRESS}",
                "Epic{Название: 'Epic 1', детали: 'Epic Detail', id :7, статус: NEW}",
                "SubTask{Название: 'SubTask 1', детали: 'SubTask Detail', id :4, статус: NEW, epicId: 3}"
        ));

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);

        var listTasks = manager.getListTasks();
        var listEpics = manager.getListEpic();
        assertEquals(2, listTasks.size(), "Должно быть загружено 2 задачи");
        assertEquals(3, listTasks.get(0).getId(), "Неправильный id, дожен быть id = 3");
        assertEquals(7, listEpics.get(0).getId(), "Неправильный id, дожен быть id = 7");
    }
}
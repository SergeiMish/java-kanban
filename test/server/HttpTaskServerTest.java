package server;

import com.google.gson.Gson;
import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static server.HttpTaskServer.getGson;

public class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private Gson gson = getGson();
    private InMemoryTaskManager manager;
    private LocalDate date;
    private LocalTime time;
    private LocalDate date1;
    private LocalTime time1;
    private LocalDate date2;
    private LocalTime time2;

    @BeforeEach
    void init() {
        manager = (InMemoryTaskManager) Managers.getDefault();
        httpTaskServer = new HttpTaskServer(manager);
        this.gson = getGson();
        date = LocalDate.now();
        time = LocalTime.now();
        date1 = LocalDate.of(2024, 10, 23);
        time1 = LocalTime.of(15, 30);
        date2 = LocalDate.of(2024, 11, 23);
        time2 = LocalTime.of(12, 30);
        Task task1 = new Task("Имя", "детали", date, time, 30, Status.NEW);
        Epic epic1 = new Epic("Имя1", "детали1", date1, time1, 30, Status.NEW);
        SubTask subTask1 = new SubTask("Имя2", "детали2", date2, time2, 30, Status.NEW, 2);
        manager.createTask(task1);
        manager.createEpic(epic1);
        manager.createSubTask(subTask1);
        httpTaskServer.start();
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        assertNotNull(manager.getListTasks(), "Задачи не возвращаются");
        assertEquals(1, manager.getListTasks().size(), "Неверное количество задач");
        assertEquals("Имя", manager.getTaskId(1).getName(), "Неверное имя задачи");
    }

    @Test
    void taskAdd() throws IOException, InterruptedException {

        Task task1 = new Task("Имя", "детали", date, time, 30, Status.NEW);

        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getEpicAndSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI epicUri = URI.create("http://localhost:8080/epic");
        HttpRequest epicRequest = HttpRequest.newBuilder().uri(epicUri).GET().build();
        HttpResponse<String> epicResponse = client.send(epicRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, epicResponse.statusCode(), "Ожидался код ответа 200 для эпиков");
        assertNotNull(manager.getListEpic(), "Эпики не возвращаются");
        assertEquals(1, manager.getListEpic().size(), "Неверное количество эпиков");
        assertEquals("Имя1", manager.getEpicId(2).getName(), "Неверное имя эпика");

        URI subtaskUri = URI.create("http://localhost:8080/subtask");
        HttpRequest subtaskRequest = HttpRequest.newBuilder().uri(subtaskUri).GET().build();
        HttpResponse<String> subtaskResponse = client.send(subtaskRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, subtaskResponse.statusCode(), "Ожидался код ответа 200 для сабтасков");
        assertNotNull(manager.getListSubTask(), "Сабтаски не возвращаются");
        assertEquals(1, manager.getListSubTask().size(), "Неверное количество сабтасков");
        assertEquals("Имя2", manager.getSubTaskId(3).getName(), "Неверное имя сабтаска");
    }

    @Test
    void epicAndSubTaskAdd() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        Epic epic1 = new Epic("Имя1", "детали1", date1, time1, 30, Status.NEW);
        String epicJson = gson.toJson(epic1);
        URI epicUrl = URI.create("http://localhost:8080/epic");
        HttpRequest epicRequest = HttpRequest.newBuilder().uri(epicUrl).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> epicResponse = client.send(epicRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, epicResponse.statusCode());

        SubTask subTask1 = new SubTask("Имя2", "детали2", date2, time2, 30, Status.NEW, 1);
        String subTaskJson = gson.toJson(subTask1);
        URI subTaskUrl = URI.create("http://localhost:8080/epic");
        HttpRequest subTaskRequest = HttpRequest.newBuilder().uri(subTaskUrl).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        HttpResponse<String> subTaskResponse = client.send(subTaskRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, subTaskResponse.statusCode());
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        int taskId = 1;
        URI deleteUrl = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest deleteRequest = HttpRequest.newBuilder().uri(deleteUrl).DELETE().build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteResponse.statusCode());
        assertTrue(deleteResponse.body().contains("Удалили Task id - " + taskId));

        int invalidTaskId = -1;
        URI invalidDeleteUrl = URI.create("http://localhost:8080/tasks/" + invalidTaskId);
        HttpRequest invalidDeleteRequest = HttpRequest.newBuilder().uri(invalidDeleteUrl).DELETE().build();
        HttpResponse<String> invalidDeleteResponse = client.send(invalidDeleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, invalidDeleteResponse.statusCode());
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        int epicId = 2;
        URI deleteEpicUrl = URI.create("http://localhost:8080/epic/" + epicId);
        HttpRequest deleteEpicRequest = HttpRequest.newBuilder().uri(deleteEpicUrl).DELETE().build();
        HttpResponse<String> deleteEpicResponse = client.send(deleteEpicRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteEpicResponse.statusCode());
        assertTrue(deleteEpicResponse.body().contains("Удалили Epic id - " + epicId));

        int invalidEpicId = -1;
        URI invalidDeleteEpicUrl = URI.create("http://localhost:8080/epic/" + invalidEpicId);
        HttpRequest invalidDeleteEpicRequest = HttpRequest.newBuilder().uri(invalidDeleteEpicUrl).DELETE().build();
        HttpResponse<String> invalidDeleteEpicResponse = client.send(invalidDeleteEpicRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, invalidDeleteEpicResponse.statusCode());
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        int subtaskId = 2;
        URI deleteSubtaskUrl = URI.create("http://localhost:8080/subtask/" + subtaskId);
        HttpRequest deleteSubtaskRequest = HttpRequest.newBuilder().uri(deleteSubtaskUrl).DELETE().build();
        HttpResponse<String> deleteSubtaskResponse = client.send(deleteSubtaskRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteSubtaskResponse.statusCode());
        assertTrue(deleteSubtaskResponse.body().contains("Удалили Subtask id - " + subtaskId));

        int invalidSubtaskId = -1;
        URI invalidDeleteSubtaskUrl = URI.create("http://localhost:8080/subtask/" + invalidSubtaskId);
        HttpRequest invalidDeleteSubtaskRequest = HttpRequest.newBuilder().uri(invalidDeleteSubtaskUrl).DELETE().build();
        HttpResponse<String> invalidDeleteSubtaskResponse = client.send(invalidDeleteSubtaskRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, invalidDeleteSubtaskResponse.statusCode());
    }

    @Test
    void testGetHistoryAndPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        URI historyUrl = URI.create("http://localhost:8080/history");
        HttpRequest historyRequest = HttpRequest.newBuilder().uri(historyUrl).GET().build();
        HttpResponse<String> historyResponse = client.send(historyRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, historyResponse.statusCode());

        assertFalse(historyResponse.body().isEmpty());

        URI prioritizedUrl = URI.create("http://localhost:8080/prioritized");
        HttpRequest prioritizedRequest = HttpRequest.newBuilder().uri(prioritizedUrl).GET().build();
        HttpResponse<String> prioritizedResponse = client.send(prioritizedRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, prioritizedResponse.statusCode());

        assertFalse(prioritizedResponse.body().isEmpty());
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }
}

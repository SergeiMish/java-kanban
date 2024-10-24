package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import interfaces.TaskManager;
import manager.Managers;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HttpTaskServer {

    private static final int PORT = 8080;

    private HttpServer server;
    public static TaskManager taskManager;



    public HttpTaskServer(TaskManager manager) {
        taskManager = manager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public static void main(String[] args) {
        taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        taskManager.getListTasks();
        httpTaskServer.start();


    }

    public void start() {
        try {
            System.out.println("Сервер запущен на порту: " + PORT);
            server = HttpServer.create();
            server.bind(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TaskHandler());
            server.createContext("/epic", new EpicHandler());
            server.createContext("/subtask", new SubTaskHandler());
            server.start();
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера");
        }
    }

    public void stop(){
        server.stop(0);
        System.out.println("Сервер остановлен на порту: " + PORT);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }
}


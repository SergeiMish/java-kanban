package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import interfaces.TaskManager;
import manager.Managers;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateAdapter;
import server.adapter.LocalDateTimeAdapter;
import server.adapter.LocalTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HttpTaskServer {

    private static final int PORT = 8080;
    public static TaskManager taskManager;
    private HttpServer server;


    public HttpTaskServer(TaskManager manager) {
        taskManager = manager;
    }

    public static void main(String[] args) {
        taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());
        return gsonBuilder.create();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void start() {
        try {
            System.out.println("Сервер запущен на порту: " + PORT);
            server = HttpServer.create();
            server.bind(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TaskHandler());
            server.createContext("/epic", new EpicHandler());
            server.createContext("/subtask", new SubTaskHandler());
            server.createContext("/history", new HistoryHandler());
            server.createContext("/prioritized", new PrioritizedHandler());
            server.start();
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера");
        }
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен на порту: " + PORT);
    }
}


package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpsServer;
import interfaces.TaskManager;
import manager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;

    private HttpsServer server;
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
        httpTaskServer.start();


    }

    public void start() {
        try {
            System.out.println("Сервер запущен на порту: " + PORT);
            server = HttpsServer.create(new InetSocketAddress( PORT), 0);
            server.createContext("/tasks", new TaskHandler());
            System.out.println("http://localhost" + PORT + "/tasks");
            server.start();
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера");
        }
    }

    public void stop(){
        server.stop(0);
        System.out.println("Сервер остановлен на порту: " + PORT);
    }
}


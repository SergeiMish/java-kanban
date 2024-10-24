package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static server.HttpTaskServer.getGson;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private final InMemoryTaskManager manager;
    private final Gson gson;

    public TaskHandler() {
        this.manager = (InMemoryTaskManager) Managers.getDefault();
        this.gson = getGson();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/tasks$", path)) {
                        manager.getListTasks();
                        String response = gson.toJson(manager.getListTasks());
                        System.out.println(response);
                        sendText(exchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getTaskId(id));
                            sendText(exchange, response);
                        } else {
                            sendNotFound(exchange, "Получен некорректный идентификатор id " + pathId);
                        }
                    } else {
                        sendNotFound(exchange, "Некорректный путь для GET запроса.");
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/tasks$", path)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        String requestBody = sb.toString();
                        Task task = gson.fromJson(requestBody, Task.class);
                        Integer i = task.getId();
                        if (i == null) {
                            if (manager.canAddTask(task)) {
                                manager.createTask(task);
                                exchange.sendResponseHeaders(201, 0);
                                sendText(exchange, "Задача создана");
                            } else {
                                sendHasInteractions(exchange, "Задачи пересекаются по времени");
                            }
                        } else {
                            if (manager.canAddTask(task)) {
                                manager.updateTask(task);
                                sendText(exchange, "Задача обновлена");
                            } else {
                                sendHasInteractions(exchange, "Задачи пересекаются по времени");
                            }
                        }
                        break;
                    } else {
                        sendNotFound(exchange, "Некорректный путь для POST запроса.");
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            manager.deleteTask(id);
                            sendText(exchange, "Удалили Task id - " + id);
                            break;
                        } else {
                            sendNotFound(exchange, "Получен некорректный идентификатор id " + pathId);
                            break;
                        }
                    } else {
                        sendNotFound(exchange, "Такого id нет");
                    }
                    break;
                }
                default:
                    sendNotFound(exchange, "Ошибка ввода, ожидалось GET,POST,DELETE, вы ввели " + method);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}


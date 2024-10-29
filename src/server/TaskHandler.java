package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import manager.Managers;
import tasks.Task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static server.HttpTaskServer.getGson;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler() {
        this.manager = Managers.getDefault();
        this.gson = getGson();
    }


    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(manager.getListTasks());
                        sendText(exchange, response);
                        break;
                    }

                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parsePathId(pathId);
                        if (id > 0) {
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
                        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                             BufferedReader br = new BufferedReader(isr)) {
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            String requestBody = sb.toString();
                            Task task = gson.fromJson(requestBody, Task.class);
                            try {
                                if (task.getId() <= 0) {
                                    manager.createTask(task);
                                    sendText(exchange, "Задача создана");
                                } else {
                                    manager.updateTask(task);
                                    sendText(exchange, "Задача обновлена");
                                }
                            } catch (IllegalArgumentException e) {
                                exchange.sendResponseHeaders(406, 0);
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
                        if (id > 0) {
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
            throw new RuntimeException("Ошибка при обработке запроса", e);
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


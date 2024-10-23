package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import manager.exeptions.TaskConflictException;
import manager.exeptions.TaskNotFoundException;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private InMemoryTaskManager manager;
    private Gson gson;

    public TaskHandler() {
        this.manager = manager;
        this.gson = gson;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/task$", path)) {
                        String response = gson.toJson(manager.getListTasks());
                        sendText(exchange, response);
                        break;
                    }
                    if (Pattern.matches("^/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getTaskId(id));
                            sendText(exchange, response);
                            break;
                        } else {
                            sendNotFound(exchange, "Получен некорректный идентификатор id " + pathId);
                        }
                    } else {
                        sendNotFound(exchange, "Такого id нет");
                        break;
                    }
                    break;
                }
                case "POST": {
                    if (Pattern.matches("^/task$", path)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        String requestBody = sb.toString();
                        Task task = gson.fromJson(requestBody, Task.class);
                            if (manager.canAddTask(task)) {
                                manager.createTask(task);
                                exchange.sendResponseHeaders(201, 0);
                            } else {
                                sendHasInteractions(exchange, " ");
                                throw new IllegalArgumentException("Задачи пересекаются по времени");
                            }
                                 if (!manager.canAddTask(task)) {
                                    manager.updateTask(task);
                                    sendText(exchange, "Задача обновлена");
                                    break;
                                }
                                 else {
                                     sendNotFound(exchange, "Ошибка обновления");
                                 }
                        sendNotFound(exchange, "Некорректный путь для POST запроса.");
                        }
                        break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/task/\\d+$", path)) {
                        String pathId = path.replaceFirst("/task/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            manager.deleteTask(id);
                            sendText(exchange, "Удалили пользователя id - " + id);
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
        } finally {
            exchange.close();
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


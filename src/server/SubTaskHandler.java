package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.SubTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static server.HttpTaskServer.getGson;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {

    private final InMemoryTaskManager manager;
    private final Gson gson;

    public SubTaskHandler() {
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
                    if (Pattern.matches("^/subtask$", path)) {
                        manager.getListSubTask();
                        String response = gson.toJson(manager.getListSubTask());
                        System.out.println(response);
                        sendText(exchange, response);
                        break;
                    }

                    if (Pattern.matches("^/subtask/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getSubTaskId(id));
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
                    if (Pattern.matches("^/subtask$", path)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        String requestBody = sb.toString();
                        SubTask subTask = gson.fromJson(requestBody, SubTask.class);
                        Integer i = subTask.getId();
                        if (i == null) { // Предполагаем, что это новая подзадача
                            if (manager.canAddTask(subTask)) {
                                manager.createSubTask(subTask);
                                exchange.sendResponseHeaders(201, 0);
                                sendText(exchange, "Подзадача создана");
                            } else {
                                sendHasInteractions(exchange, "Задачи пересекаются по времени");
                            }
                        } else {
                            if (manager.canAddTask(subTask)) {
                                manager.updateSubTask(subTask);
                                exchange.sendResponseHeaders(200, 0);
                                sendText(exchange, "Подзадача обновлена");
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
                    if (Pattern.matches("^/subtask/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtask/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            manager.deleteSubTask(id);
                            sendText(exchange, "Удалили Subtask id - " + id);
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
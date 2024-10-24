package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.Epic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static server.HttpTaskServer.getGson;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    private final InMemoryTaskManager manager;
    private final Gson gson;

    public EpicHandler() {
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
                    if (Pattern.matches("^/epic$", path)) {
                        manager.getListEpic();
                        String response = gson.toJson(manager.getListEpic());
                        System.out.println(response);
                        sendText(exchange, response);
                        break;
                    }

                    if (Pattern.matches("^/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getEpicId(id));
                            sendText(exchange, response);
                        } else {
                            sendNotFound(exchange, "Получен некорректный идентификатор id " + pathId);
                        }
                    } else {
                        sendNotFound(exchange, "Некорректный путь для GET запроса.");
                    }

                    if (Pattern.matches("^/epic/\\d+$/subtask", path)) {
                        String pathId = path.replaceFirst("/epic/", "")
                                .replaceFirst("/subtask", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(manager.getListSubTasksOfEpic(id));
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
                    if (Pattern.matches("^/epic$", path)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        String requestBody = sb.toString();
                        Epic epic = gson.fromJson(requestBody, Epic.class);
                        Integer i = epic.getId();
                        if (i == null) {
                            manager.createEpic(epic);
                            exchange.sendResponseHeaders(201, 0);
                            sendText(exchange, "Эпик создан");
                        } else {
                            manager.updateEpic(epic);
                            sendText(exchange, "Эпик обновлен");
                        }
                        break;
                    } else {
                        sendNotFound(exchange, "Некорректный путь для POST запроса.");
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/epic/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epic/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            manager.deleteEpic(id);
                            sendText(exchange, "Удалили Epic id - " + id);
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

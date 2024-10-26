package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;
import manager.Managers;

import java.util.regex.Pattern;

import static server.HttpTaskServer.getGson;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager manager;
    private final Gson gson;

    public PrioritizedHandler() {
        this.manager = Managers.getDefault();
        this.gson = getGson();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("GET".equals(method)) {
                if (Pattern.matches("^/prioritized$", path)) {
                    String response = gson.toJson(manager.getPrioritizedTasks());
                    sendText(exchange, response);
                } else {
                    sendNotFound(exchange, "");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
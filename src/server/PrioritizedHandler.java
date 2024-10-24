package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;
import manager.Managers;

import java.io.IOException;
import java.util.regex.Pattern;

import static server.HttpTaskServer.getGson;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    private final InMemoryTaskManager manager;
    private final Gson gson;

    public PrioritizedHandler() {
        this.manager = (InMemoryTaskManager) Managers.getDefault();
        this.gson = getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (exchange) {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("GET".equals(method)) {
                if (Pattern.matches("^/prioritized$", path)) {
                    manager.getPrioritizedTasks();
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
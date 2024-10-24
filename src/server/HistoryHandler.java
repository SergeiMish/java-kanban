package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    private InMemoryTaskManager manager;
    private Gson gson;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}
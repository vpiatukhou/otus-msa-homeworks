package homework;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import homework.dao.OrderDao;
import homework.model.CreateOrderDto;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderHttpHandlerImpl implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(OrderHttpHandlerImpl.class.getName());

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final String USERNAME_HEADER = "X-User";

    private final OrderDao orderDao = new OrderDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (HTTP_METHOD_GET.equalsIgnoreCase(exchange.getRequestMethod())) {
                get(exchange);
            } else if (HTTP_METHOD_POST.equalsIgnoreCase(exchange.getRequestMethod())) {
                post(exchange);
            } else {
                exchange.sendResponseHeaders(HTTP_BAD_REQUEST, -1);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred.", e);
            exchange.sendResponseHeaders(HTTP_INTERNAL_ERROR, -1);
        } finally {
            exchange.close();
        }
    }

    private void get(HttpExchange exchange) throws IOException {
        var username = exchange.getRequestHeaders().getFirst(USERNAME_HEADER);
        var idempotencyKey = orderDao.getOrInsertIdempotencyKey(username);
        var orders = orderDao.getOrders(username);

        var rootJson = new HashMap<>();
        rootJson.put("next_idempotency_key", idempotencyKey);
        var orderJsons = new ArrayList<>();
        rootJson.put("orders", orderJsons);
        for (var order : orders) {
            var orderJson = new HashMap<>();
            orderJson.put("from", order.pickup());
            orderJson.put("to", order.destination());
            orderJsons.add(orderJson);
        }
        var body = new Gson().toJson(rootJson);
        exchange.sendResponseHeaders(HTTP_OK, body.getBytes().length);

        try (var writer = new OutputStreamWriter(exchange.getResponseBody())) {
            writer.append(body);
        }
    }

    private void post(HttpExchange exchange) throws IOException {
        var username = exchange.getRequestHeaders().getFirst(USERNAME_HEADER);
        if (username == null || username.isBlank()) {
            exchange.sendResponseHeaders(HTTP_UNAUTHORIZED, -1);
        } else {
            try (var reader = new InputStreamReader(exchange.getRequestBody())) {
                var requestMap = new Gson().fromJson(reader, Map.class);
                var idempotencyKey = Objects.toString(requestMap.get("idempotency_key"), "");
                var pickup = Objects.toString(requestMap.get("from"), "");
                var destination = Objects.toString(requestMap.get("to"), "");
                orderDao.createOrder(new CreateOrderDto(idempotencyKey, pickup, destination, username));
            }
            exchange.sendResponseHeaders(HTTP_OK, -1);
        }
    }
}

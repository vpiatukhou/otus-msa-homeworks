package homework.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import homework.dao.AccountDao;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class RegisterHttpHandlerImpl extends BaseHttpHandlerImpl {

    private final AccountDao accountDao;

    public RegisterHttpHandlerImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    void handleInternal(HttpExchange exchange) throws IOException {
        if (HTTP_METHOD_POST.equalsIgnoreCase(exchange.getRequestMethod())) {
            try (var is = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                var request = new Gson().fromJson(is, Map.class);
                var username = Objects.toString(request.get(REQUEST_JSON_USERNAME), "");
                var password = Objects.toString(request.get(REQUEST_JSON_PASSWORD), "");
                if (accountDao.isAccountExists(username, password)) {
                    exchange.sendResponseHeaders(HTTP_BAD_REQUEST, -1);
                } else {
                    accountDao.createAccount(username, password);
                    exchange.sendResponseHeaders(HTTP_OK, -1);
                }
            }
        } else {
            exchange.sendResponseHeaders(HTTP_METHOD_NOT_ALLOWED, -1);
        }
    }
}

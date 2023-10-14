package homework.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import homework.dao.AccountDao;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class LoginHttpHandlerImpl extends BaseHttpHandlerImpl {

    private final Map<String, String> usernamesBySessionIds;
    private final AccountDao accountDao;

    public LoginHttpHandlerImpl(Map<String, String> usernamesBySessionIds, AccountDao accountDao) {
        this.usernamesBySessionIds = usernamesBySessionIds;
        this.accountDao = accountDao;
    }

    @Override
    void handleInternal(HttpExchange exchange) throws IOException {
        if (HTTP_METHOD_POST.equalsIgnoreCase(exchange.getRequestMethod())) {
            String username;
            String password;
            LOGGER.info("Login - reading a request body.");
            try (var is = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                var request = new Gson().fromJson(is, Map.class);
                username = Objects.toString(request.get(REQUEST_JSON_USERNAME), "");
                password = Objects.toString(request.get(REQUEST_JSON_PASSWORD), "");
            }

            LOGGER.info("Login - checking if the account exists.");
            if (accountDao.isAccountExists(username, password)) {
                var sessionId = UUID.randomUUID().toString();
                usernamesBySessionIds.put(sessionId, username);
                var cookie = new Cookie();
                cookie.set(SESSION_ID_COOKIE, sessionId);
                cookie.updateCookieHeaders(exchange.getResponseHeaders());
                exchange.sendResponseHeaders(HTTP_OK, -1);
            } else {
                exchange.sendResponseHeaders(HTTP_UNAUTHORIZED, -1);
            }
        } else {
            exchange.sendResponseHeaders(HTTP_METHOD_NOT_ALLOWED, -1);
        }
    }
}

package homework;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Map;

class AuthHttpHandlerImpl extends BaseHttpHandlerImpl {

    private final Map<String, String> usernamesBySessionIds;

    AuthHttpHandlerImpl(Map<String, String> usernamesBySessionIds) {
        this.usernamesBySessionIds = usernamesBySessionIds;
    }

    @Override
    void handleInternal(HttpExchange exchange) throws IOException {
        if (HTTP_METHOD_GET.equalsIgnoreCase(exchange.getRequestMethod())) {
            var sessionId = new Cookie(exchange.getRequestHeaders()).get(SESSION_ID_COOKIE);
            var username = sessionId.isBlank() ? "" : usernamesBySessionIds.getOrDefault(sessionId, "");
            if (username.isBlank()) {
                exchange.sendResponseHeaders(HTTP_UNAUTHORIZED, -1);
            } else {
                exchange.getResponseHeaders().add(USERNAME_HEADER, username);
                exchange.sendResponseHeaders(HTTP_OK, -1);
            }
        } else {
            exchange.sendResponseHeaders(HTTP_METHOD_NOT_ALLOWED, -1);
        }
    }
}

package homework;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class LogoutHttpHandlerImpl extends BaseHttpHandlerImpl {

    @Override
    void handleInternal(HttpExchange exchange) throws IOException {
        if (HTTP_METHOD_POST.equalsIgnoreCase(exchange.getRequestMethod())) {
            var cookie = new Cookie();
            cookie.set(SESSION_ID_COOKIE, "");
            cookie.updateCookieHeaders(exchange.getResponseHeaders());
            exchange.sendResponseHeaders(HTTP_OK, -1);
        } else {
            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, -1);
        }
    }
}

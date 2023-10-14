package homework.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseHttpHandlerImpl implements HttpHandler {

    static final Logger LOGGER = Logger.getLogger(BaseHttpHandlerImpl.class.getName());

    static final String HTTP_METHOD_GET = "GET";
    static final String HTTP_METHOD_POST = "POST";
    static final int HTTP_OK = 200;
    static final int HTTP_BAD_REQUEST = 400;
    static final int HTTP_UNAUTHORIZED = 401;
    static final int HTTP_METHOD_NOT_ALLOWED = 405;
    static final int HTTP_INTERNAL_ERROR = 500;
    static final String SESSION_ID_COOKIE = "sessionId";
    static final String USERNAME_HEADER = "X-User";
    static final String REQUEST_JSON_USERNAME = "username";
    static final String REQUEST_JSON_PASSWORD = "password";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LOGGER.info("Received a request.");
        try {
            handleInternal(exchange);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred.", e);
            exchange.sendResponseHeaders(HTTP_INTERNAL_ERROR, -1);
        } finally {
            exchange.close();
        }
        LOGGER.info("Request processing finished.");
    }

    abstract void handleInternal(HttpExchange exchange) throws IOException;
}

package homework;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfileHttpHandlerImpl implements HttpHandler {

    private static final Logger LOGGER = Logger.getLogger(ProfileHttpHandlerImpl.class.getName());

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_PUT = "PUT";
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private static final String USERNAME_HEADER = "X-User";

    private final ProfileDao profileDao = new ProfileDao();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (HTTP_METHOD_GET.equalsIgnoreCase(exchange.getRequestMethod())) {
                get(exchange);
            } else if (HTTP_METHOD_PUT.equalsIgnoreCase(exchange.getRequestMethod())) {
                put(exchange);
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
        var optional = profileDao.getProfileByUsername(username);

        var jsonAsMap = new HashMap<>();
        jsonAsMap.put("username", username);
        if (optional.isPresent()) {
            var profile = optional.get();
            jsonAsMap.put("first_name", profile.firstName());
            jsonAsMap.put("last_name", profile.lastName());
            jsonAsMap.put("email", profile.email());
            jsonAsMap.put("phone", profile.phone());
        }
        var body = new Gson().toJson(jsonAsMap);

        exchange.sendResponseHeaders(HTTP_OK, body.getBytes().length);

        try (var writer = new OutputStreamWriter(exchange.getResponseBody())) {
            writer.append(body);
        }
    }

    private void put(HttpExchange exchange) throws IOException {
        var username = exchange.getRequestHeaders().getFirst(USERNAME_HEADER);
        if (username == null || username.isBlank()) {
            exchange.sendResponseHeaders(HTTP_BAD_REQUEST, -1);
        } else {
            try (var reader = new InputStreamReader(exchange.getRequestBody())) {
                var requestMap = new Gson().fromJson(reader, Map.class);
                var profile = new Profile(username,
                        Objects.toString(requestMap.get("first_name"), ""),
                        Objects.toString(requestMap.get("last_name"), ""),
                        Objects.toString(requestMap.get("email"), ""),
                        Objects.toString(requestMap.get("phone"), ""));
                if (profileDao.getProfileByUsername(username).isPresent()) {
                    profileDao.updateProfile(profile);
                } else {
                    profileDao.createProfile(profile);
                }
            }
            exchange.sendResponseHeaders(HTTP_OK, -1);
        }
    }
}

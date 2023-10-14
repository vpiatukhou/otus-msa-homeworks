package homework.http;

import com.sun.net.httpserver.Headers;

import java.util.HashMap;
import java.util.Map;

class Cookie {

    private static final String COOKIE_HEADER = "Cookie";
    private static final String SET_COOKIE_HEADER = "Set-Cookie";
    private static final String PAIR_DELIMITER = ";";
    private static final String KEY_VALUE_DELIMITER = "=";

    private final Map<String, String> keyValues = new HashMap<>();

    Cookie() {
    }

    Cookie(Headers headers) {
        var cookieHeaderValue = headers.getFirst(COOKIE_HEADER);
        if (cookieHeaderValue != null) {
            var pairs = cookieHeaderValue.split(PAIR_DELIMITER);
            for (var pair : pairs) {
                var keyValue = pair.split(KEY_VALUE_DELIMITER);
                if (keyValue.length == 2) {
                    keyValues.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }
    }

    void updateCookieHeaders(Headers headers) {
        for (var keyValue : keyValues.entrySet()) {
            headers.add(SET_COOKIE_HEADER, keyValue.getKey() + KEY_VALUE_DELIMITER + keyValue.getValue());
        }
    }

    String get(String key) {
        return keyValues.getOrDefault(key, "");
    }

    void set(String key, String value) {
        keyValues.put(key, value);
    }

    void remove(String key) {
        keyValues.remove(key);
    }
}

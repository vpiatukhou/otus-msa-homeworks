package homework;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final int SERVER_PORT = 8000;

    private final AccountDao accountDao = new AccountDao();

    private final Map<String, String> usernamesBySessionIds = new HashMap<>();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        var server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext("/auth", new AuthHttpHandlerImpl(usernamesBySessionIds));
        server.createContext("/register", new RegisterHttpHandlerImpl(accountDao));
        server.createContext("/login", new LoginHttpHandlerImpl(usernamesBySessionIds, accountDao));
        server.createContext("/logout", new LogoutHttpHandlerImpl());
        server.start();
    }
}

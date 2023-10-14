package homework.http;

import com.sun.net.httpserver.HttpServer;
import homework.dao.AccountDao;
import homework.dao.LiquibaseDao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Map<String, String> usernamesBySessionIds = new HashMap<>();

    public void start() throws IOException {
        new LiquibaseDao().update();

        var accountDao = new AccountDao();
        var server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/auth", new AuthHttpHandlerImpl(usernamesBySessionIds));
        server.createContext("/register", new RegisterHttpHandlerImpl(accountDao));
        server.createContext("/login", new LoginHttpHandlerImpl(usernamesBySessionIds, accountDao));
        server.createContext("/logout", new LogoutHttpHandlerImpl());
        server.start();
    }
}

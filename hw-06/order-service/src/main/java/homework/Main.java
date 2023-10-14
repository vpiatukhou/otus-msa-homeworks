package homework;

import com.sun.net.httpserver.HttpServer;
import homework.dao.LiquibaseDao;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        new LiquibaseDao().update();

        var server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext("/orders", new OrderHttpHandlerImpl());
        server.start();
    }
}

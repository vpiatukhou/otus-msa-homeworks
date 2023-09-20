package homework;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    private void run() throws IOException {
        var server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext("/me", new ProfileHttpHandlerImpl());
        server.start();
    }
}

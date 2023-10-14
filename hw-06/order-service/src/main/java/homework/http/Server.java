package homework.http;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public void start() throws IOException {
        var server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/orders", new OrderHttpHandlerImpl());
        server.start();
    }
}

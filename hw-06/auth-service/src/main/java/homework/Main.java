package homework;

import homework.dao.LiquibaseDao;
import homework.http.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new LiquibaseDao().update();
        new Server().start();
    }
}

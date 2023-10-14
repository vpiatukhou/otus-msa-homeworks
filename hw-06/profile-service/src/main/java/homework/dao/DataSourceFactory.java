package homework.dao;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {

    private static final String DB_URL = "DB_URL";
    private static final String DB_USERNAME = "DB_USERNAME";
    private static final String DB_PASSWORD = "DB_PASSWORD";

    private DataSourceFactory() {
    }

    static DataSource createDataSource() {
        var ds = new PGSimpleDataSource();
        ds.setUrl(System.getenv(DB_URL));
        ds.setUser(System.getenv(DB_USERNAME));
        ds.setPassword(System.getenv(DB_PASSWORD));
        return ds;
    }
}

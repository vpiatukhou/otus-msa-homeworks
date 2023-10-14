package homework.dao;

import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.sql.SQLException;

public class LiquibaseDao {

    private static final String CHANGELOG = "changelog.sql";

    private final DataSource dataSource = DataSourceFactory.createDataSource();

    public void update() {
        try (var connection = dataSource.getConnection()) {
            var database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection));
            var liquibase = new Liquibase(CHANGELOG, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}

package homework;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

class AccountDao {

    private static final String DB_URL = "DB_URL";
    private static final String DB_USERNAME = "DB_USERNAME";
    private static final String DB_PASSWORD = "DB_PASSWORD";

    private static final String CREATE_ACCOUNT = "INSERT INTO account(username, password) VALUES(?, ?)";
    private static final String IS_ACCOUNT_EXISTS = "SELECT 1 FROM account WHERE username = ? AND password = ?";

    private final DataSource dataSource;

    public AccountDao() {
        PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
        pgDataSource.setUrl(System.getenv(DB_URL));
        pgDataSource.setUser(System.getenv(DB_USERNAME));
        pgDataSource.setPassword(System.getenv(DB_PASSWORD));
        dataSource = pgDataSource;
    }

    void createAccount(String username, String password) {
        try (var statement = dataSource.getConnection().prepareStatement(CREATE_ACCOUNT)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isAccountExists(String username, String password) {
        try (var statement = dataSource.getConnection().prepareStatement(IS_ACCOUNT_EXISTS)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (var result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

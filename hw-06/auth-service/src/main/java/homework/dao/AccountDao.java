package homework.dao;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AccountDao {

    private static final String CREATE_ACCOUNT = "INSERT INTO account(username, password) VALUES(?, ?)";
    private static final String IS_ACCOUNT_EXISTS = "SELECT 1 FROM account WHERE username = ? AND password = ?";

    private final DataSource dataSource;

    public AccountDao() {
        dataSource = DataSourceFactory.createDataSource();
    }

    public void createAccount(String username, String password) {
        try (var statement = dataSource.getConnection().prepareStatement(CREATE_ACCOUNT)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAccountExists(String username, String password) {
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

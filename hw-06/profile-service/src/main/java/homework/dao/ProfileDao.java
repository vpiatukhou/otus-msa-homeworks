package homework.dao;

import homework.model.Profile;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

public class ProfileDao {

    private static final String INSERT_PROFILE = """
        INSERT INTO profile(first_name, last_name, email, phone, username) VALUES(?, ?, ?, ?, ?)
        """;
    private static final String UPDATE_PROFILE = """
        UPDATE profile SET
               first_name = ?,
               last_name = ?,
               email = ?,
               phone = ?
         WHERE username = ?
        """;
    private static final String SELECT_PROFILE = """
        SELECT first_name, last_name, email, phone FROM profile WHERE username = ?
        """;

    private static final String DB_URL = "DB_URL";
    private static final String DB_USERNAME = "DB_USERNAME";
    private static final String DB_PASSWORD = "DB_PASSWORD";

    private final DataSource dataSource;

    public ProfileDao() {
        PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
        pgDataSource.setUrl(System.getenv(DB_URL));
        pgDataSource.setUser(System.getenv(DB_USERNAME));
        pgDataSource.setPassword(System.getenv(DB_PASSWORD));
        dataSource = pgDataSource;
    }

    public void createProfile(Profile profile) {
        try (var statement = dataSource.getConnection().prepareStatement(INSERT_PROFILE)) {
            statement.setString(1, profile.firstName());
            statement.setString(2, profile.lastName());
            statement.setString(3, profile.email());
            statement.setString(4, profile.phone());
            statement.setString(5, profile.username());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProfile(Profile profile) {
        try (var statement = dataSource.getConnection().prepareStatement(UPDATE_PROFILE)) {
            statement.setString(1, profile.firstName());
            statement.setString(2, profile.lastName());
            statement.setString(3, profile.email());
            statement.setString(4, profile.phone());
            statement.setString(5, profile.username());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Profile> getProfileByUsername(String username) {
        try (var statement = dataSource.getConnection().prepareStatement(SELECT_PROFILE)) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            if (result.next()) {
                return Optional.of(new Profile(
                        username,
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("email"),
                        result.getString("phone")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

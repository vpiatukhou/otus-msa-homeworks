package homework.dao;

import homework.model.CreateOrderDto;
import homework.model.OrderDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDao {

    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());

    private static final String PG_ERROR_UNIQUE_VIOLATION = "23505";

    private static final String SELECT_ORDERS = """
            SELECT pickup, destination, ordered_by FROM orders WHERE ordered_by = ?;
            """;

    private static final String SELECT_IDEMPOTENCY_KEY = """
            SELECT idempotency_key FROM idempotency_key_pool WHERE username = ?;
            """;

    private static final String INSERT_IDEMPOTENCY_KEY = """
            INSERT INTO idempotency_key_pool(idempotency_key, username) VALUES(?, ?);
            """;

    private static final String INSERT_ORDER = """
            INSERT INTO orders(idempotency_key, pickup, destination, ordered_by)
                 VALUES(?, ?, ?, ?);
            """;

    private static final String UPDATE_IDEMPOTENCY_KEY = """
            UPDATE idempotency_key_pool SET idempotency_key = ? WHERE username = ?;
            """;

    private final DataSource dataSource = DataSourceFactory.createDataSource();

    public List<OrderDto> getOrders(String username) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SELECT_ORDERS)) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            List<OrderDto> orders = new ArrayList<>();
            while (result.next()) {
                orders.add(new OrderDto(result.getString("pickup"),
                        result.getString("destination")));
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOrInsertIdempotencyKey(String username) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(SELECT_IDEMPOTENCY_KEY)) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            if (result.next()) {
                return result.getString("idempotency_key");
            }
            var key = UUID.randomUUID().toString();
            insertIdempotencyKey(connection, username, key);
            return key;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertIdempotencyKey(Connection connection, String username, String key) {
        try (var statement = connection.prepareStatement(INSERT_IDEMPOTENCY_KEY)) {
            statement.setString(1, key);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createOrder(CreateOrderDto orderDto) {
        try (var connection = dataSource.getConnection()) {
            try(var insertStatement = connection.prepareStatement(INSERT_ORDER);
                var updateStatement = connection.prepareStatement(UPDATE_IDEMPOTENCY_KEY)) {
                connection.setAutoCommit(false);
                connection.setSavepoint();

                insertStatement.setString(1, orderDto.idempotencyKey());
                insertStatement.setString(2, orderDto.pickup());
                insertStatement.setString(3, orderDto.destination());
                insertStatement.setString(4, orderDto.orderedBy());

                updateStatement.setString(1, UUID.randomUUID().toString());
                updateStatement.setString(2, orderDto.orderedBy());

                insertStatement.executeUpdate();
                updateStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                if (PG_ERROR_UNIQUE_VIOLATION.equals(e.getSQLState())) {
                    LOGGER.log(Level.WARNING, "Unique constraint violation on inserting order.");
                    return false;
                } else {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}

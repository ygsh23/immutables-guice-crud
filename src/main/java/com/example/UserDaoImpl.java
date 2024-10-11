package com.example;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

@Singleton
public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final DataSource dataSource;

    @Inject
    public UserDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, user.name());
            statement.setString(3, user.email());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Exception: ", e);
        }
    }

    @Override
    public Optional<User> getUserById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapRowToUser(resultSet));
            }
        } catch (SQLException e) {
            log.error("Exception: ", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                users.add(mapRowToUser(resultSet));
            }
        } catch (SQLException e) {
            log.error("Exception: ", e);
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.name());
            statement.setString(2, user.email());
            statement.setString(3, user.id().orElse(null));
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Exception: ", e);
        }
    }

    @Override
    public void deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Exception: ", e);
        }
    }

    private User mapRowToUser(ResultSet resultSet) throws SQLException {
        return ImmutableUser.builder()
                .id(resultSet.getString("id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .build();
    }
}
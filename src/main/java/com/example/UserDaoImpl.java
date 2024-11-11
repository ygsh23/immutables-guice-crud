package com.example;

import com.google.inject.Singleton;

import java.util.List;
import java.util.Optional;

import com.google.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Singleton
public class UserDaoImpl implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
    private final Jdbi jdbi;

    @Inject
    public UserDaoImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void createUser(ImmutableUser user) {
        String sql = "INSERT INTO users (id, name, email) VALUES (:id, :name, :email)";
        try {
            jdbi.useHandle(handle -> handle.createUpdate(sql).bind("id", UUID.randomUUID().toString()).bind("name", user.name()).bind("email", user.email()).execute());
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    @Override
    public Optional<ImmutableUser> getUserById(String id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        try {
            return jdbi.withHandle(handle -> handle.createQuery(sql).bind("id", id).map(new UserMapper()).findFirst());
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        return Optional.empty();
    }

    @Override
    public List<ImmutableUser> getAllUsers() {
        String sql = "SELECT * FROM users";

        try {
            return jdbi.withHandle(handle -> handle.createQuery(sql).map(new UserMapper()).list());
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        return List.of();
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET name = :name, email = :email WHERE id = :id";
        try {
            jdbi.useHandle(handle -> handle.createUpdate(sql).bind("id", user.id()).bind("name", user.name()).bind("email", user.email()).execute());
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }

    @Override
    public void deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = :id";
        try {
            jdbi.useHandle(handle -> handle.createUpdate(sql).bind("id", id).execute());
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
    }
}
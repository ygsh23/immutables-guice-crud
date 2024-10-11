package com.example;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void createUser(User user);
    Optional<User> getUserById(String id);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(String id);
}
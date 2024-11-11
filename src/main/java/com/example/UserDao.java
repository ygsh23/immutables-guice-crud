package com.example;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    void createUser(ImmutableUser user);
    Optional<ImmutableUser> getUserById(String id);
    List<ImmutableUser> getAllUsers();
    void updateUser(User user);
    void deleteUser(String id);
}
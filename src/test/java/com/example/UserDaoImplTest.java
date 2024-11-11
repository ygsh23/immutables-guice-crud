package com.example;

import com.google.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementContext;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserDaoImplTest {
    @Mock
    private Jdbi jdbi;

    private UserDaoImpl userDao;

    @BeforeMethod
    public void setUp() {
        jdbi = Mockito.mock(Jdbi.class);
        userDao = new UserDaoImpl(jdbi);
    }

    @Test
    public void testCreateUser() {
        ImmutableUser user = ImmutableUser.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();

        userDao.createUser(user);

        verify(jdbi).useHandle(any());
    }

//    @Test
//    public void testGetUserById() {
//        String userId = UUID.randomUUID().toString();
//        ImmutableUser user = ImmutableUser.builder()
//                .id(Optional.of(userId))
//                .name("Jane Doe")
//                .email("jane@example.com")
//                .build();
//
//        when(jdbi.withHandle(any())).thenAnswer(invocation -> {
//            ResultSet rs = mock(ResultSet.class);
//            when(rs.getString("id")).thenReturn(userId);
//            when(rs.getString("name")).thenReturn(user.name());
//            when(rs.getString("email")).thenReturn(user.email());
//            return List.of(user);
//        });
//
//        Optional<ImmutableUser> result = userDao.getUserById(userId);
//
//        verify(jdbi).withHandle(any());
//        assert result.isPresent();
//        assert result.get().name().equals("Jane Doe");
//    }

    @Test
    public void testGetAllUsers() {
        ImmutableUser user1 = ImmutableUser.builder()
                .id(Optional.of(UUID.randomUUID().toString()))
                .name("User 1")
                .email("user1@example.com")
                .build();

        ImmutableUser user2 = ImmutableUser.builder()
                .id(Optional.of(UUID.randomUUID().toString()))
                .name("User 2")
                .email("user2@example.com")
                .build();

        when(jdbi.withHandle(any())).thenAnswer(invocation -> List.of(user1, user2));

        List<ImmutableUser> users = userDao.getAllUsers();

        assert users.size() == 2;
        assert users.get(0).name().equals("User 1");
        assert users.get(1).name().equals("User 2");
    }

    @Test
    public void testUpdateUser() {
        User user = Mockito.mock(User.class);
        when(user.id()).thenReturn(Optional.of("123"));
        when(user.name()).thenReturn("Updated User");
        when(user.email()).thenReturn("updated@example.com");

        userDao.updateUser(user);

        verify(jdbi).useHandle(any());
    }

    @Test
    public void testDeleteUser() {
        String userId = "123";

        userDao.deleteUser(userId);

        verify(jdbi).useHandle(any());
    }

    @Test
    public void testDeleteUser_withException() {
        String userId = "123";
        Handle handle = Mockito.mock(Handle.class);

        // Simulate an exception when SQL is executed
        doThrow(new RuntimeException("SQL exception")).when(jdbi).useHandle(any());

        // Call the method and verify no exception is thrown, it is caught
        userDao.deleteUser(userId);

        // Verify that the exception was logged (if you have a logger)
        // You could mock the logger and verify it was called like:
        // verify(logger).error(anyString(), any(Exception.class));
    }
}

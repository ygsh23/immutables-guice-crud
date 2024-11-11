package com.example;

import jakarta.ws.rs.core.Response;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserResourceTest {
    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserResource userResource;

    @BeforeMethod
    public void setUp() {
        userDao = Mockito.mock(UserDao.class);
        userResource = new UserResource(userDao);
    }

    @Test
    public void testCreateUser() {
        ImmutableUser user = ImmutableUser.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();

        Response response = userResource.createUser(user);

        assert response.getStatus() == Response.Status.CREATED.getStatusCode();
        verify(userDao).createUser(user);
    }

    @Test
    public void testGetUser() {
        String userId = "123";
        ImmutableUser user = ImmutableUser.builder()
                .id(Optional.of(userId))
                .name("Jane Doe")
                .email("jane@example.com")
                .build();

        when(userDao.getUserById(userId)).thenReturn(Optional.of(user));

        Response response = userResource.getUser(userId);

        assert response.getStatus() == Response.Status.OK.getStatusCode();
        assert response.getEntity() instanceof ImmutableUser;
        verify(userDao).getUserById(userId);
    }

    @Test
    public void testGetUserNotFound() {
        String userId = "123";

        when(userDao.getUserById(userId)).thenReturn(Optional.empty());

        Response response = userResource.getUser(userId);

        assert response.getStatus() == Response.Status.NOT_FOUND.getStatusCode();
        verify(userDao).getUserById(userId);
    }

    @Test
    public void testGetAllUsers() {
        ImmutableUser user1 = ImmutableUser.builder()
                .id(Optional.of("1"))
                .name("User 1")
                .email("user1@example.com")
                .build();

        ImmutableUser user2 = ImmutableUser.builder()
                .id(Optional.of("2"))
                .name("User 2")
                .email("user2@example.com")
                .build();

        when(userDao.getAllUsers()).thenReturn(List.of(user1, user2));

        List<ImmutableUser> users = userResource.getAllUsers();

        assert users.size() == 2;
        verify(userDao).getAllUsers();
    }

    @Test
    public void testUpdateUser() {
        String userId = "123";
        User user = Mockito.mock(User.class);
        when(user.name()).thenReturn("Updated User");
        when(user.email()).thenReturn("updated@example.com");

        Response response = userResource.updateUser(userId, user);

        assert response.getStatus() == Response.Status.OK.getStatusCode();
        verify(userDao).updateUser(any());
    }

    @Test
    public void testDeleteUser() {
        String userId = "123";

        Response response = userResource.deleteUser(userId);

        assert response.getStatus() == Response.Status.NO_CONTENT.getStatusCode();
        verify(userDao).deleteUser(userId);
    }
}

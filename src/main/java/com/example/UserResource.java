package com.example;

import com.google.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDao userDao;

    @Inject
    public UserResource(UserDao userDao) {
        this.userDao = userDao;
    }

    @POST
    public Response createUser(ImmutableUser user) {
        userDao.createUser(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String id) {
        Optional<ImmutableUser> user = userDao.getUserById(id);
        return user.map(u -> Response.ok(u).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public List<ImmutableUser> getAllUsers() {
        return userDao.getAllUsers();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") String id, User user) {
        ImmutableUser updatedUser = ImmutableUser.builder()
                .id(id)
                .name(user.name())
                .email(user.email())
                .build();

        userDao.updateUser(updatedUser);
        return Response.ok(updatedUser).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") String id) {
        userDao.deleteUser(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
package controllers;

import daomanagers.KweetManager;
import daomanagers.UserManager;
import domain.Kweet;
import domain.User;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    private UserManager userManager;

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        return Response.ok(userManager.GetUser(id)).build();
    }

    @GET
    @Path("/all")
    public Response get()
    {
        return Response.ok(userManager.GetUsers()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUser(User user) {
        if (userManager.GetUser(user.getName()) != null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        user = userManager.AddUser(user);

        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeUser(@PathParam("id") long id) {
        if (userManager.GetUser(id) == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        userManager.RemoveUser(id);

        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUser(User user) {
        if (userManager.GetUser(user.getId()) == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        user = userManager.EditUser(user);

        return Response.ok(user).build();
    }

    @POST
    @Path("/{id}/follow/{other}")
    public Response follow(@PathParam("id") long id, @PathParam("other") long other) {
        return Response.ok(userManager.Follow(id, other)).build();
    }

    @POST
    @Path("/{id}/unfollow/{other}")
    public Response unfollow(@PathParam("id") long id, @PathParam("other") long other) {
        return Response.ok(userManager.Unfollow(id, other)).build();
    }

    @GET
    @Path("/{id}/following")
    public Response following(@PathParam("id") long id)
    {
        return Response.ok(userManager.GetFollowing(id)).build();
    }

    @GET
    @Path("/{id}/followers")
    public Response followers(@PathParam("id") long id)
    {
        return Response.ok(userManager.GetFollowers(id)).build();
    }
}

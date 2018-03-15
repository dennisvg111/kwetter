package controllers;

import daomanagers.KweetManager;
import daomanagers.UserManager;
import domain.Kweet;
import domain.User;

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
    private
    UserManager userManager;

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        return Response.ok(userManager.GetUser(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postKweet(User user) {
        if (userManager.GetUser(user.getName()) != null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        user = userManager.AddUser(user);

        return Response.ok(user).build();
    }
}

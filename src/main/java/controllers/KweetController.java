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

@Path("/kweets")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class KweetController {
    @Inject
    private KweetManager kweetManager;

    @Inject
    private UserManager userManager;

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") long id) {
        return Response.ok(kweetManager.getKweet(id)).build();
    }

    @GET
    @Path("/all")
    public Response get()
    {
        return Response.ok(kweetManager.getKweets()).build();
    }

    @GET
    @Path("/feed/{id}")
    public Response getFeed(@PathParam("id") long id)
    {
        User user = userManager.GetUser(id);
        if (user == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(kweetManager.GetFeed(user)).build();
    }

    @GET
    @Path("/search/{query}")
    public Response getFeed(@PathParam("query") String query)
    {
        return Response.ok(kweetManager.Search(query)).build();
    }

    @GET
    @Path("/users/{id}")
    public Response getKweeted(@PathParam("id") long id)
    {
        User user = userManager.GetUser(id);
        if (user == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(kweetManager.GetKweetsFromUser(user)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postKweet(Kweet kweet) {
        if (kweet == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Kweet postedKweet = kweetManager.AddKweet(kweet);

        return Response.ok(postedKweet).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editKweet(Kweet kweet) {
        if (kweet == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Kweet postedKweet = kweetManager.EditKweet(kweet);

        return Response.ok(postedKweet).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeKweet(Kweet kweet) {
        if (kweet == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        kweetManager.RemoveKweet(kweet);

        return Response.ok().build();
    }
}
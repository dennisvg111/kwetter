package controllers;

import daomanagers.KweetManager;
import domain.Kweet;

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
    private
    KweetManager kweetManager;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postKweet(Kweet kweet) {
        if (kweet == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Kweet postedKweet = kweetManager.AddKweet(kweet);

        return Response.ok(postedKweet).build();
    }
}
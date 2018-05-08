package controllers;

import com.google.common.hash.Hashing;
import com.mysql.cj.core.util.StringUtils;
import daomanagers.KweetManager;
import daomanagers.UserManager;
import domain.Kweet;
import domain.Role;
import domain.User;
import domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import jdk.nashorn.internal.objects.annotations.Getter;
import jwt.JwtTokenNeeded;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

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
    @Path("/find/{name}")
    public Response find(@PathParam("name") String name) {
        return Response.ok(userManager.FindUser(name)).build();
    }

    @GET
    @Path("/all")
    public Response get()
    {
        return Response.ok(userManager.GetUsers()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response loginUser(User user, @Context UriInfo uriInfo)
    {
        User verifiedUser = userManager.FindUser(user.getName());
        if (verifiedUser == null || StringUtils.isNullOrEmpty(user.getHashedPassword()))
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String sha256HexPassword = Hashing.sha256()
                .hashString(user.getHashedPassword(), StandardCharsets.UTF_8)
                .toString();

        if (!sha256HexPassword.equals(verifiedUser.getHashedPassword()))
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, 12);

        // https://stormpath.com/blog/jwt-java-create-verify
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("some-secret-key");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        String token = Jwts.builder()
                .setId(String.valueOf(verifiedUser.getId()))
                .setSubject(verifiedUser.getName())
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(cal.getTime())
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();

        return Response.ok("\"Bearer " + token + "\"").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUser(User user) {
        if (userManager.FindUser(user.getName()) != null)
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
    @JwtTokenNeeded
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

    @PUT
    @Path("/{id}/role/{role}")
    public Response followers(@PathParam("id") long id, @PathParam("role") String role) { return Response.ok(userManager.SetRoles(userManager.GetUser(id), new Role(role))).build(); }
}

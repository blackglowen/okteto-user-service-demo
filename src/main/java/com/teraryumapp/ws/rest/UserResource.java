package com.teraryumapp.ws.rest;

import java.util.List;

// import javax.annotation.security.PermitAll;
// import javax.annotation.security.RolesAllowed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

// import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import org.jboss.resteasy.reactive.RestPath;
import io.smallrye.mutiny.Uni;

import com.teraryumapp.ws.rest.entities.User;
import com.teraryumapp.ws.rest.services.UserService;
import com.teraryumapp.ws.rest.exceptions.UserResourceException;

@Path("resources/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;

    }

    @GET
    // @RolesAllowed({ "USER", "ADMIN" })
    @Operation(summary = "Get user collection", description = "Get a collection of all available users")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))))
    public Uni<List<User>> getAllUsers() {
        return userService.getAllUsers();
    }

    @GET
    @Path("/{id}")
    // @RolesAllowed({ "USER", "ADMIN" })
    @Operation(summary = "Get a user", description = "Retrieve a specific user by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @APIResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResourceException.class)))
    })
    public Uni<User> getUser(@RestPath Long id) {
        return userService.getUser(id);
    }

    @POST
    @Transactional
   // @PermitAll
    @Operation(summary = "Add a user", description = "Create a user and persists into database")
    @APIResponses(value = @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))))
    public Uni<Response> create(User user) {

        return userService.create(user);

    }

    @PUT
    @Path("/{id}")
    @Transactional
    // @RolesAllowed("ADMIN")
    @Operation(summary = "Update a user", description = "Update an existing user by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @APIResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResourceException.class)))
    })
    public Uni<Response> update(@RestPath Long id, User user) {
        return userService.update(id, user);

    }

    @DELETE
    @Path("/{id}")
    @Transactional
    // @RolesAllowed("ADMIN")
    @Operation(summary = "Delete a user", description = "Delete a user by id")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Success"),
            @APIResponse(responseCode = "404", description="User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResourceException.class)))
    })
    public Uni<Response> delete(@RestPath Long id) {
        return userService.delete(id);
    }

    @GET
    @Path("/search/{name}")
    // @RolesAllowed({ "USER", "ADMIN" })
    @Operation(summary = "Find a user", description = "Find or search a specific user by name")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @APIResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResourceException.class)))
    })
    public Uni<User> search(@RestPath("name") String name) {
        return userService.search(name);
    }

    @GET
    @Path("/count")
    // @RolesAllowed({ "ADMIN" })
    @Operation(summary = "Get user count", description = "Retrieve the user count from the database")
    @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class)))    
    public Uni<Long> count() {
        return userService.count();
    }

}

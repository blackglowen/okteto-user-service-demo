package com.teraryumapp.ws.rest.exceptions.handlers;

import javax.inject.Inject;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.CompositeException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.teraryumapp.ws.rest.UserResource;
import com.teraryumapp.ws.rest.exceptions.UserResourceException;

/**
 * Create a HTTP response from an exception.
 *
 * Response Example:
 *
 * <pre>
 * HTTP/1.1 422 Unprocessable Entity
 * Content-Length: 111
 * Content-Type: application/json
 *
 * {
 *     "code": 422,
 *     "error": "User id was not set on request.",
 *     "exception": "com.teraryum.ws.rest.exceptions.UserResourceException"
 * }
 * </pre>
 */
@Provider
public class UserResourceExceptionHandler implements ExceptionMapper<Exception> {

    @Inject
    ObjectMapper objectMapper;

    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

    @Override
    public Response toResponse(Exception exception) {
        LOGGER.error("Failed to handle user resource request", exception);
        Throwable throwable = exception;
        int code = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        if (throwable instanceof UserResourceException) {
            code = ((UserResourceException) throwable).getResponse().getStatus();
        }

        // This is a Mutiny exception and it happens, for example, when we try to insert
        // a new user but the user is already in the database
        if (throwable instanceof CompositeException) {
            throwable = ((CompositeException) throwable).getCause();
        }

        ObjectNode exceptionJson = objectMapper.createObjectNode();
        exceptionJson.put("exception", exception.getClass().getName());
        exceptionJson.put("code", code);

        if (exception.getMessage() != null) {
            exceptionJson.put("error", exception.getMessage());
        }

        return Response.status(code)
                .entity(exceptionJson)
                .build();
    }

}
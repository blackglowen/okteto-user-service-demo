package com.teraryumapp.ws.rest.exceptions;

import javax.ws.rs.WebApplicationException;

public class UserResourceException extends WebApplicationException {

    public UserResourceException(String message, int code) {
        super(message, code);
    }
}
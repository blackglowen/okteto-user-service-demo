package com.teraryumapp.ws.rest.services;

import java.util.List;
import javax.ws.rs.core.Response;
import io.smallrye.mutiny.Uni;

import com.teraryumapp.ws.rest.entities.User;

public interface UserService {

    public Uni<List<User>> getAllUsers();

    public Uni<User> getUser(Long id);

    public Uni<Response> create(User user);

    public Uni<Response> update(Long id, User user);

    public Uni<Response> delete(Long id);

    public Uni<User> search(String name);

    public Uni<Long> count();
}
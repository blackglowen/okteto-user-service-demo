package com.teraryumapp.ws.rest.services.impl;

import java.util.List;
import java.net.URI;
import java.time.LocalDateTime;
import javax.ws.rs.core.Response;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import io.smallrye.mutiny.Uni;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.reactive.panache.Panache;


import com.teraryumapp.ws.rest.services.UserService;
import com.teraryumapp.ws.rest.repositories.UserRepository;
import com.teraryumapp.ws.rest.entities.User;
import com.teraryumapp.ws.rest.exceptions.UserResourceException;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Uni<List<User>> getAllUsers() {

        return userRepository.listAll();

    }

    @Override
    public Uni<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Uni<Response> create(User user) {
        if (user.id != null) {
            throw new UserResourceException("The user's id was invalidly set on request.",
                    Response.Status.BAD_REQUEST.getStatusCode());
        }

        user.password = BcryptUtil.bcryptHash(user.password);

        return Panache.<User>withTransaction(user::persist)
                .onItem().transform(inserted -> Response.created(URI.create("/users/" + inserted.id)).build());
    }

    @Override
    public Uni<Response> update(Long id, User user) {
        User entity = (User) User.findById(id);
        if (entity == null || user == null) {
            throw new UserResourceException("User with id of " + id + " does not exist.",
                    Response.Status.NOT_FOUND.getStatusCode());
        }

        return Panache
                .withTransaction(() -> User.<User>findById(id)
                        .onItem().ifNotNull().invoke(
                                foundUser -> {
                                    foundUser.firstName = user.firstName;
                                    foundUser.lastName = user.lastName;
                                    foundUser.updatedAt = LocalDateTime.now();
                                }))
                .onItem().ifNotNull().transform(foundUser -> Response.ok(foundUser).build())
                .onItem().ifNull().continueWith(Response.ok().status(Response.Status.NOT_FOUND)::build);
    }

    @Override
    public Uni<Response> delete(Long id) {
        return Panache.withTransaction(() -> User.deleteById(id))
                .map(deleted -> deleted
                        ? Response.ok().status(Response.Status.NO_CONTENT).build()
                        : Response.ok().status(Response.Status.NOT_FOUND).build());
    }

    @Override
    public Uni<User> search(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Uni<Long> count() {
        return userRepository.count();
    }

}

package com.teraryumapp.ws.rest.repositories;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.time.LocalDateTime;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import com.teraryumapp.helpers.Status;
import com.teraryumapp.ws.rest.entities.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Uni<User> findByName(String name) {
        return find("fname like ?1 or lname like ?2 or username like ?3", name, name, name).firstResult();
    }

    public Uni<List<User>> findActiveUsers() {
        return list("status", Status.ACTIVE);
    }

    public void softDelete(Long id) {
        User.update("status = ?1 where id = ?2", Status.DELETED, id);
        User.update("deleted_at = ?1 where id = ?2", LocalDateTime.now(), id);
    }

}

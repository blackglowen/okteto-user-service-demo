package com.teraryumapp.ws.rest.entities;

import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.teraryumapp.helpers.Status;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Username;



@Entity
@Cacheable
@Table(name="users")
public class User extends Model{

    @GeneratedValue
    public UUID uuid = UUID.randomUUID();

    @Column(name = "fname", nullable = false)
    @NotBlank
    @Size(max = 256)
    public String firstName;

    @Column(name = "lname", nullable = false)
    @NotBlank
    @Size(max = 256)
    public String lastName;

    @Column(unique = true, nullable = false)
    @Username
    public String username;

    @Column(name = "password", nullable = false)
    @Password
    public String password;

    @Column(unique = true, nullable = false)
    public String email;

    @Column(unique = true)
    public String phone;

    public LocalDate birthdate;

    @Column(nullable = true)
    public String gender;

    @Column(nullable = false)
    public String status = Status.ACTIVE.name();

    @Column(name = "created_at", nullable = false)
    public LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    public LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "verified_at")
    public LocalDateTime verifiedAt;

    @Column(name = "deleted_at")
    public LocalDateTime deletedAt;

    public String getFullname(){
        return (this.firstName +  ' ' + this.lastName).toUpperCase();
    }

}

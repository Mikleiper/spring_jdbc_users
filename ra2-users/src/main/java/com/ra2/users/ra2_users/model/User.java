package com.ra2.users.ra2_users.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {
    //atributs
    private Long id;
    private String name;
    private String description;
    private String email;
    private String password;
    private LocalDateTime ultimAcces;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;

    //constructors
    public User(){
    }

    public User(LocalDateTime dataCreated, LocalDateTime dataUpdated, String description, String email, Long id, String name, String password, LocalDateTime ultimAcces) {
        this.dataCreated = dataCreated;
        this.dataUpdated = dataUpdated;
        this.description = description;
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
        this.ultimAcces = ultimAcces;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getUltimAcces() {
        return ultimAcces;
    }

    public void setUltimAcces(Timestamp ultimAcces) {
        if (ultimAcces != null) this.ultimAcces = ultimAcces.toLocalDateTime();
    }

    public LocalDateTime getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Timestamp dataCreated) {
        if (dataCreated != null) this.dataCreated = dataCreated.toLocalDateTime();
    }

    public LocalDateTime getDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(Timestamp dataUpdated) {
        if (dataUpdated != null) this.dataUpdated = dataUpdated.toLocalDateTime();
    }    
}

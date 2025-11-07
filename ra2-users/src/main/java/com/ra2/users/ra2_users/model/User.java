package com.ra2.users.ra2_users.model;

import java.sql.Timestamp;

public class User {
    //atributs
    private Long id;
    private String name;
    private String description;
    private String email;
    private String password;
    private Timestamp ultimAcces;
    private Timestamp dataCreated;
    private Timestamp dataUpdated;
    private String image_path;

    //constructors
    public User(){
    }

    public User(Timestamp dataCreated, Timestamp dataUpdated, String description, String email, Long id, String name, String password, Timestamp ultimAcces, String image_path) {
        this.dataCreated = dataCreated;
        this.dataUpdated = dataUpdated;
        this.description = description;
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
        this.ultimAcces = ultimAcces;
    }

    // gets i posts    
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

    public Timestamp getUltimAcces() {
        return ultimAcces;
    }

    public void setUltimAcces(Timestamp ultimAcces) {
        this.ultimAcces = ultimAcces;
    }

    public Timestamp getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(Timestamp dataCreated) {
        this.dataCreated = dataCreated;
    }

    public Timestamp getDataUpdated() {
        return dataUpdated;
    }

    public void setDataUpdated(Timestamp dataUpdated) {
        this.dataUpdated = dataUpdated;
    }

    public String getImagePath() {
        return image_path;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }
    
    // ToSting
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", description=" + description + ", email=" + email + ", password=" + password + ", ultimAcces=" + ultimAcces + ", dataCreated=" + dataCreated + ", dataUpdated=" + dataUpdated + ", image_path=" + image_path + "]";
    }
}

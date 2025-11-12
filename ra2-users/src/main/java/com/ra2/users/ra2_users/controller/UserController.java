package com.ra2.users.ra2_users.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.service.UserService;

/**
 * Controlador REST per gestionar operacions CRUD sobre la taula "user".
 * Proporciona endpoints per crear, consultar, actualitzar i eliminar usuaris.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    // Endpoint per inserir un nou usuari a la base de dades
    @PostMapping("/users")
    public ResponseEntity<String> postUser(@RequestBody User user) {   
        userService.save(user);  //cridem metode save definit al UserService
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuari creat correctament: " + user.getName());
    }

    // Endpoint per obtenir tots els usuaris de la base de dades. Retorna llista d’usuaris o null si no n’hi ha cap
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);// Retorna tots els usuaris trobats amb status 200 OK
    }

    // Endpoint per obtenir un usuari concret pel seu ID
    @GetMapping("users/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user =  userService.getUser(id); // Cerquem l’usuari pel seu ID
        return ResponseEntity.status(HttpStatus.OK).body(user);  // Retorna l'usuari trobat amb status 200 OK
    }

    // Endpoint per actualitzar totes les dades d’un usuari
    @PutMapping("users/{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable Long user_id, @RequestBody User user) { //<?> pq pot retornar dos tipus: o String si no existeix  o User si existeix i es modifica satisfactoriament l'User
        User actulitzat = userService.updateUser(user_id, user);
        return (actulitzat == null) ?  null : ResponseEntity.status(HttpStatus.OK).body("L'usuari amb id " + user_id + "actulizat satisfactoriament");
    }

    // Endpoint per modificar només el nom d’un usuari
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<?> updateName(@PathVariable Long user_id, @RequestParam String name) {  //<?> pq pot retornar dos tipus: o String si no existeix  o User si existeix i modifica el nom
        User actulitzat = userService.updateUserName(user_id, name);
        return (actulitzat == null) ?  null : ResponseEntity.ok().body("El nom de l'usuari amb id " + user_id + "actulizat satisfactoriament");
    }

    // Endpoint per eliminar un usuari pel seu ID.
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        return (userService.deleteUser(user_id))? ResponseEntity.ok().body("Usuari amb id=" + user_id + " eliminat correctament de la base de dades.") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'usuari cercat per esborrar no existeix");
    }

    //Endpoint per pujar imatge
    @PostMapping("/users/{user_id}/image")
    public ResponseEntity<String> uploadUserImage(@PathVariable Long user_id, @RequestParam MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(userService.saveUserImage(user_id, imageFile));
    }
    
    //pujar csv
    @PostMapping("/users/upload-csv")
    public ResponseEntity<String> postUsersCsv(@RequestBody User user, @RequestParam MultipartFile csvFile) {
        
        return ResponseEntity.status(HttpStatus.OK).body("$numeorusuaris + usuaris creats satisfactoriament");
    }
    

}

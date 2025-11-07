package com.ra2.users.ra2_users.controller;

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

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

/**
 * Controlador REST per gestionar operacions CRUD sobre la taula "user".
 * Proporciona endpoints per crear, consultar, actualitzar i eliminar usuaris.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    // Injecció automàtica del UserRepository per accedir a la base de dades
    @Autowired
    UserRepository userRepository;

    // Endpoint per inserir un nou usuari a la base de dades
    @PostMapping("/users")
    public ResponseEntity<String> postUser(@RequestBody User user) {   
        userRepository.save(user);  //cridem metode save definit al UserRepository
        String msg = String.format("Usuari inserit correctament a la taula User amb aquestes dades: " + "name=%s, description=%s, email=%s",user.getName(), user.getDescription(), user.getEmail()); //creem un string amb format per poder posar al missatge les dades introduides
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    // Endpoint per obtenir tots els usuaris de la base de dades. Retorna llista d’usuaris o null si no n’hi ha cap
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) { 
            return ResponseEntity.status(HttpStatus.OK).body(null); // No hi ha cap usuari: retornem null però amb status 200 OK
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);// Retorna tots els usuaris trobats amb status 200 OK
    }

    // Endpoint per obtenir un usuari concret pel seu ID
    @GetMapping("users/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable Long user_id) {
        User user =  userRepository.findOne(user_id); // Cerquem l’usuari pel seu ID
        if(user == null){
            return ResponseEntity.status(HttpStatus.OK).body(null); // No troba l'usuari: retornem null però amb status 200 OK
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);  // Retorna l'usuari trobat amb status 200 OK
    }

    // Endpoint per actualitzar totes les dades d’un usuari
    @PutMapping("users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable Long user_id, @RequestBody User user) {
        userRepository.modifyUser(user, user_id);  // Actualitza l’usuari a la BBDD
        String msg = String.format("Usuari amd id = %d i nom = %s actulizat satisfactoriament", user.getId(), user.getName());
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    // Endpoint per modificar només el nom d’un usuari
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<?> updateName(@PathVariable Long user_id, @RequestParam String name) {  //<?> pq pot retornar dos tipus: o String si no existeix  o User si existeix i modifica el nom
        User user = userRepository.findOne(user_id);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'usuari cercat no existeix");
        user.setName(name); // Modifiquem només el nom
        userRepository.modifyUser(user, user_id);
        return ResponseEntity.ok().body(userRepository.findOne(user_id));
    }

    // Endpoint per eliminar un usuari pel seu ID.
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        User user = userRepository.findOne(user_id);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'usuari cercat per esborrar no existeix");
        userRepository.deleteById(user_id); // Si existeix, l’esborrem
        return ResponseEntity.ok().body("Usuari amb id=" + user_id + " eliminat correctament de la base de dades.");
    }
}

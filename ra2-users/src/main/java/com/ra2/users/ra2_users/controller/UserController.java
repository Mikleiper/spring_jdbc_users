package com.ra2.users.ra2_users.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;



@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<String> crearUsuari(@RequestBody User user) {   
        // Validació de camps not null
        if (user.getName() == null || user.getDescription() == null ||
            user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuari no creat perque falten camps obligatoris (name, description, email, password)"); // ResponseEntity per indicar l’status i la informació a través del body
        }

        try {
            userRepository.crearUsuari(user);  //cridem metode crearUsuari definit al UserRepository
            String msg = String.format(
            "Usuari inserit correctament a la taula User amb aquestes dades: " +
            "name=%s, description=%s, email=%s",
            user.getName(), user.getDescription(), user.getEmail()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear l’usuari: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Object> obtenirUsuaris() {
        try {
            List<User> usuaris = userRepository.findAll();
            if (usuaris == null || usuaris.isEmpty()) { // No hi ha cap usuari: retornem null però amb status 200 OK
                return ResponseEntity.status(HttpStatus.OK).body("null");
            }
            return ResponseEntity.status(HttpStatus.OK).body(usuaris);// Retorna tots els usuaris trobats amb status 200 OK
        } catch (Exception e) {// En cas d’error, retornem 500 però sense llençar excepcions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtenir els usuaris: " + e.getMessage());
        }
    }
    

    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> esborrarUsuari(@PathVariable Long user_id) {
        try {
            int filesAfectades = userRepository.deleteById(user_id);

            if (filesAfectades == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s’ha trobat cap usuari amb id=" + user_id);
            }

            return ResponseEntity.status(HttpStatus.OK).body("Usuari amb id=" + user_id + " eliminat correctament de la base de dades.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar l’usuari amb id=" + user_id + ": " + e.getMessage());
        }
    }
    
}

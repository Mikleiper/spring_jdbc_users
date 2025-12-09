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

import com.ra2.users.ra2_users.logging.CustomLogging;
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
    private UserService userService;

    @Autowired
    private CustomLogging customLogging;

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
    public ResponseEntity<String> updateUser(@PathVariable Long user_id, @RequestBody User user) { 
        int actulitzat = userService.updateUser(user_id, user);
        return (actulitzat == 0) ?  ResponseEntity.status(HttpStatus.OK).body("No s'ha efectuat cap canvi") : ResponseEntity.status(HttpStatus.OK).body(String.format("L'usuari amb nom %s s'ha actulizat satisfactoriament", user.getName()));
    }

    // Endpoint per modificar només el nom d’un usuari
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> updateName(@PathVariable Long user_id, @RequestParam String name) { 
        int actulitzat = userService.updateUser(user_id, name);
        return (actulitzat == 0) ?  ResponseEntity.status(HttpStatus.OK).body("No s'ha efectuat cap canvi") : ResponseEntity.status(HttpStatus.OK).body(String.format("S'ha actulitzat el nom de l'usuari amb id %d satisfactoriament", user_id));
    }

    // Endpoint per eliminar un usuari pel seu ID.
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long user_id) {
        int esborrat = userService.deleteUser(user_id);
        return (esborrat > 0)? ResponseEntity.status(HttpStatus.OK).body("Usuari amb id=" + user_id + " eliminat correctament de la base de dades.") : ResponseEntity.status(HttpStatus.OK).body("No s'ha esborrat res, l'usuari cercat no existeix");
    }

    //Endpoint per pujar imatge
    @PostMapping("/users/{user_id}/image")
    public ResponseEntity<String> uploadUserImage(@PathVariable Long user_id, @RequestParam MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(userService.saveUserImage(user_id, imageFile));
    }
    
    //pujar csv
    @PostMapping("/users/upload-csv")
    public ResponseEntity<String> postUsersCsv(@RequestParam MultipartFile csvFile) {
        int nRegsitres = userService.saveUsers(csvFile);
        // segons el numero q ens retorna saveUsers ho traduim i retornem amb ResponseEntity
        if (nRegsitres == -1){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No s'ha afegit cap usuari perquè no s'ha pogut llegir l'arxiu.");
        }
        if (nRegsitres == -2){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("S'han afegit els usuaris, però no s'ha guardat el fitxer csv processat.");
        }
        if (nRegsitres == 0){
            return ResponseEntity.status(HttpStatus.OK).body("No s'ha afegit cap usuari perquè el fitxer csv no contenia cap");
        }        
        return ResponseEntity.status(HttpStatus.OK).body(String.format("%d usuaris creats satisfactoriament",nRegsitres));
    }

    @PostMapping("/users/upload-json")
    public ResponseEntity<String> postUsersJson(@RequestParam MultipartFile jsonFile) {
        int registre = userService.saveUsersJson(jsonFile);
        // segons el numero q ens retorna saveUsersJson ho traduim i retornem amb ResponseEntity
        if (registre == -1) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Contingut del Json incorrecte. No hi ha control OK");
        }
        if (registre == -2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No s'ha pogut guardar l'arxiu Json.");
        }
        if (registre == -3) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Contingut del Json incorrecte. No coincideix nombre d'usuaris continguts i valor de count");
        }
        if (registre == -4) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al llegir l'arxiu Json");
        }
        if (registre == -5) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No es pot desar l'arxiu Json");
        }
        return ResponseEntity.status(HttpStatus.OK).body(String.format("%d usuaris creats satisfactoriament", registre));
    } 
}

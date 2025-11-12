package com.ra2.users.ra2_users.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // inserir un nou usuari a la base de dades
    public void save(User user) {
        userRepository.save(user);
    }

    // Obtenir tots
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return (users == null || users.isEmpty()) ? null : users;
    }

    // Obtenir 1 usuari pel seu id
    public User getUser(Long id){
        return userRepository.findOne(id);
    }

    //Modifca 1 usuari sencer
    public User updateUser(Long id, User user) {
        User existeix = getUser(id);
        if (existeix == null) {
            return null;
        }
        // Actualitzem els camps amb les noves dades
        existeix.setName(user.getName());
        existeix.setDescription(user.getDescription());
        existeix.setEmail(user.getEmail());
        existeix.setPassword(user.getPassword());
        existeix.setImagePath(user.getImagePath());

        userRepository.modifyUser(existeix, id);
        return getUser(id); // Retornem l’usuari actualitzat
    }

    //Modifca només nom
    public User updateUserName(Long id, String newName){
        User existeix = getUser(id);
        if (existeix == null) {
            return null;
        }
        // Actualitzem el nom amb les noves dades
        existeix.setName(newName);

        userRepository.modifyUser(existeix, id);
        return getUser(id);
    }

    //Esborrem usuari per id
    public boolean deleteUser(Long id) {
        User existeix = getUser(id);
        if (existeix == null) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    public String saveUserImage(Long id, MultipartFile imageFile) throws IOException{
        User user = userRepository.findOne(id);
        
        if(user != null){
            Path novaCarpeta = Paths.get("src/main/resources/public/images");  //CREEM NOVA CARPETA //.get és = a new File en NIO2
            if (Files.notExists(novaCarpeta)) {   //si existeix
                Files.createDirectories(novaCarpeta);  //executem i creem carpeta
            }
            String fileName = imageFile.getOriginalFilename();  //Retorna el nom original del fitxer tal com el tenia l’usuari en el seu sistema
            Path destination = novaCarpeta.resolve(fileName);  //Guaradarem la imatge amb aquest nom
            /* imageFile.getInputStream() retorna un InputStream sobre el binari que ha pujat el client (el fitxer temporal de MultipartFile). No carrega tot el fitxer a la memòria: es llegeix en streaming.
            Files.copy(InputStream, Path, …) llegeix tots els bytes de l’InputStream i els escriu al Path destination.
            StandardCopyOption.REPLACE_EXISTING indica a NIO.2 que, si destination ja existeix, el reemplaça (no només el trunca).*/
            try (InputStream in = imageFile.getInputStream()) {
                Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            
            String relativePath = "/images/" + fileName;  // NO guardem la ruta absoluta del sistema d'arxius-això és quan guardem físicament l'arxiu en un discdur, SÍ guardem la ruta relativa per la BBDD
            userRepository.updateImagePath(id, relativePath);

            return "Imatge pujada correctament. Ruta: " + relativePath;
        } else {
            return "Error al guardar la imatge:";
        }
    }
    
    public int saveUsers(User user, MultipartFile imageFile) throws IOException{

        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream())))
    
    }
}

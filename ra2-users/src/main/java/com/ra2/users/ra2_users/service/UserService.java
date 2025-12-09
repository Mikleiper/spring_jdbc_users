package com.ra2.users.ra2_users.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ra2.users.ra2_users.logging.CustomLogging;
import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private CustomLogging customLogging;

    private static final String CLASS_NAME = "UserService";

    // inserir un nou usuari a la base de dades
    public int save(User user) {
        customLogging.info(CLASS_NAME, "createUser", "Creant un usuari");
        try {
            userRepository.save(user);
        } catch (Exception er) {
            customLogging.error(CLASS_NAME, "createUser", "L'user amb nom: " + user.getName() + " no s'ha creat correctament.");
            return 0;
        }
        customLogging.info(CLASS_NAME, "createUser", "User creat correctament");
        return 1;
    }

    // Obtenir tots
    public List<User> findAll() {
        customLogging.info(CLASS_NAME, "findAll", "Consultant tots els users");
        List<User> users = userRepository.findAll();
        return (users == null || users.isEmpty()) ? null : users;
    }

    // Obtenir 1 usuari pel seu id
    public User getUser(Long id) throws IOException{
        customLogging.info(CLASS_NAME, "getUserbyId", "Consultant l'user amb id: " + id);
        User user = userRepository.findOne(id);
        if (user == null) customLogging.error(CLASS_NAME, "getUserbyId", "L'user amb id: " + id + " no existeix");
        return user;
    }

    //Modifca 1 usuari sencer
    public int updateUser(Long id, User user) {
        customLogging.info(CLASS_NAME, "updateAllUser", "Modificant l'user amb id: " + user.getId());
        int result = userRepository.updateUser(user, id);
        if (result == 0) {
            customLogging.error(CLASS_NAME, "updateAllUser", "L'user amb id: " + user.getId() + " no existeix.");
        } else {
            customLogging.info(CLASS_NAME, "updateAllUser", "User modificat correctament.");
        }
        return result;
    }

    //Modifca només nom
    public int updateUser(long id, String name){
        customLogging.info(className, "updateUser", "Modificant l'user amb id: " + id);
        int result = userRepository.updateUser(id, name);
        if (result == 0) {
            customLogging.error(CLASS_NAME, "updateAllUser", "L'user amb id: " + user.getId() + " no existeix.");
        } else {
            customLogging.info(CLASS_NAME, "updateAllUser", "User modificat correctament.");
        }
        return result;
    }

    //Esborrem usuari per id
    public int deleteUser(Long id) {
        customLogging.info(CLASS_NAME, "deleteUser", "Borrant l'user amb id: " + id);
        int resultat = userRepository.deleteById(id);
        if (resultat == 0) {
            customLogging.error(CLASS_NAME, "deleteUser", "L'user amb id: " + id + " no existeix.");
        } else {
            customLogging.info(CLASS_NAME, "deleteUser", "L'user amb id: " + id + " s'ha borrat correctament.");
        }
        return resultat;
    }

    public String saveUserImage(Long id, MultipartFile imageFile) throws IOException{
        customLogging.info(CLASS_NAME, "uploadImage", "Afegint la imatge" + imageFile + "de l'user amb id: " + id);
        User user = userRepository.findOne(id);
        
        if(user != null){
            Path novaCarpeta = Paths.get("uploads/images");  //CREEM NOVA CARPETA //.get és = a new File en NIO2
            if (Files.notExists(novaCarpeta)) {   //si no existeix la carpeta
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

            customLogging.info(CLASS_NAME, "uploadImage", "Actualitzant la imatge de l'user amb id: " + id);    
            return "Imatge pujada correctament. Ruta: " + relativePath;
        } else {
            return "Error al guardar la imatge:";
        }
    }
    
    public int saveUsers(MultipartFile csvFile){
        int conta = 0;
        //controlem internament error d'accès-lectura del csv
        try(BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))){
            String linia;
            int nLinia = 0;
            while ((linia = br.readLine()) != null){
                nLinia++;
                if (nLinia == 1) continue; //saltem 1ªlinea del csv on no hi ha usuaris i només hi han headers
                if (linia.trim().isEmpty()) continue; //salta linies buides
                String[] user = linia.split(",");
                Timestamp now = new Timestamp(System.currentTimeMillis());                
                User usuari = new User(user[0],user[1],user[2],user[3],now, now, now);
                userRepository.save(usuari);     
                conta++;                           
            }
        } catch (IOException e){
            return -1;
        }
        //creem carpeta si no existeix i guardem el csv i controlem internamente rror per guardar csv a resources  
        try{
            Path directoryPath = Paths.get("uploads/csv_processed");
            Files.createDirectories(directoryPath);

            Path filePath = directoryPath.resolve(csvFile.getOriginalFilename());
            Files.write(filePath, csvFile.getBytes());
        } catch (IOException e){
            return -2;
        }
        // Retornem registres creats
        return conta;        
    }

    public int saveUsersJson(MultipartFile jsonFile){
        int conta = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        try{
            JsonNode arrel = mapper.readTree(jsonFile.getInputStream()); //llegim fitxerJson
            JsonNode data = arrel.path("data");  //accedim al node data, això depen de l'estrucutra del json
            int count = data.path("count").asInt();  // llegim el nombre de count per comparar amb nombre d'usuaris q contè el Json
            String control = data.path("control").asText(); //Idem amb control
            if (!control.equals("OK")){ //comprovem OK al control
                return -1;
            }
            JsonNode users = data.path("users");
            if (users.size() != count) {
                return -3;
            }
            //dins de users llegim els paramatres de cada ususari
            for (JsonNode user: users){
                String name = user.path("name").asText();
                String descripcio = user.path("description").asText();
                String email = user.path("email").asText();
                String contrasenya = user.path("password").asText();

                User usuari = new User(name, descripcio,email,contrasenya,now,now,now);
                // Afegim usuari a la base de dades
                try { 
                    userRepository.save(usuari);
                    conta++;
                } catch (Exception e){
                    return -2;
                }
            }
            
        } catch (IOException e) {
            return -4;
        } 

        try{ //Guardem l'arxiu JSON a la carpeta json_processed i controlem internamente error
            Path directoryPath = Paths.get("uploads/json_processed"); // Crear la carpeta si no existe
            Files.createDirectories(directoryPath);
            Path filePath = directoryPath.resolve(jsonFile.getOriginalFilename());            
            Files.write(filePath, jsonFile.getBytes());        
        }catch (IOException e){
            return -5;
        }
        return conta;
    }    
}

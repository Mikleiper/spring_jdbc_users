package com.ra2.users.ra2_users.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Crear usuari
    public void save(User user) {
        userRepository.save(user);
    }

    // Obtenir tots
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public String saveUserImage(Long id, MultipartFile imageFile){
        User user = userRepository.findOne(id);

        if(user != null){
            
            return "he trobat l'usuari";
        } else {
            return "no existeix l'ususari";       
        }
        
    }    

    public List<User> getUser(){
        return userRepository.findAll();
    }

    public int addUser(){
        int numReg = userRepository.save("Paco", 33);
        return numReg;
    }
}

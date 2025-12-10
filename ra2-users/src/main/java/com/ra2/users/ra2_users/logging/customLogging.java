package com.ra2.users.ra2_users.logging;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class CustomLogging {

    public CustomLogging(){
    }

    private static final String LOG_DIRECTORY = "logs/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    //mètode intern per gestionar la cració del directori, nom dle fitxer i contingut dle log
    private void writeToFile(String msg){
        String LOG_FILE = LOG_DIRECTORY + String.format("aplicacio-%s-%s-%s.log", LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth());
        Path logPath = Paths.get(LOG_DIRECTORY + LOG_FILE);
        try{ 
            if (logPath.getParent() != null && !Files.exists(logPath.getParent())) {
                Files.createDirectories(logPath.getParent());            }          

            try(BufferedWriter bw = Files.newBufferedWriter(logPath,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                    bw.write(msg);
                    bw.newLine(); 
                }
        }catch (IOException e) {
            System.err.println("ERROR escrivint al fitxer de log; "+ e.getMessage());
        }
    }

    // mètode per gestionars missatges d'error al log
    public void error(String className, String method, String errorMsg, Exception e){
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] ERROR - %s - %s - %s", timestamp, className, method, errorMsg);        
        if(e != null){
            logEntry += " - Exception: " + e.getMessage();
        }
        writeToFile(logEntry);
        System.out.println(logEntry);
    }  

    // mètode per gestionars missatges d'informació al log
    public void info(String className, String method, String infoMsg){
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] INFO - %s - %s - %s", timestamp, className, method, infoMsg);
        writeToFile(logEntry);
        System.out.println(logEntry);
    }

}

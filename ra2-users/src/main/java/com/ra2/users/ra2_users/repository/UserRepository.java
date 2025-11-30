package com.ra2.users.ra2_users.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra2.users.ra2_users.model.User;

/**
 * Classe Repository encarregada de comunicar-se amb la base de dades.
 * Conté totes les operacions CRUD sobre la taula "user".
 * pot injectar-se amb @Autowired en altres classes->UserController
 */
@Repository
public class UserRepository {

    /* Injecció automàtica de JdbcTemplate (Spring ja crea aquest objecte)
    * JdbcTemplate és la classe que permet executar consultes SQL de forma simplificada.
    */
    @Autowired
    private JdbcTemplate jdbcTemplate;    
    /**
     * Classe interna per convertir (mapar) cada fila del ResultSet (resultat SQL)
     * en un objecte User de Java.
    */
    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException{
             // Creem un nou objecte User per a cada fila retornada pel SELECT
            User user = new User();
            // Assignem cada columna de la taula al seu atribut corresponent
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setDescription(rs.getString("description"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUltimAcces(rs.getTimestamp("ultimacces"));
            user.setDataCreated(rs.getTimestamp("dataCreated"));
            user.setDataUpdated(rs.getTimestamp("dataUpdated"));
            user.setImagePath(rs.getString("image_path"));
            return user; // Retornem l’objecte User creat
        }
    }

    // Adherir nous users. 
    public int save(User user) {        
        return jdbcTemplate.update("INSERT INTO user (name, description, email, password, ultimAcces, dataCreated, dataUpdated) VALUES (?,?,?,?,?,?,?)",user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), user.getUltimAcces(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
    }

    // Retorna tots els usuaris en format llista d'Users
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, new UserRowMapper()); // query() executa el SELECT i utilitza el RowMapper per convertir cada fila a un User
    }

    // Busca un usuari pel seu ID
    public User findOne(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        List<User> results = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return results.isEmpty()? null : results.get(0); 
    }

    // Actualitza totes les dades d’un usuari.
    public int updateUser(User user, Long id) {
        String sql = "UPDATE user SET name = ?, description = ?, email = ?, password = ?, dataUpdated = ?, WHERE id = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), new Timestamp(System.currentTimeMillis()), id);
    }

    // Actualitza el nom d’un usuari.
    public int updateUser(long id, String name){
        String sql = "UPDATE user set name = ?, data_actualitzat = ? where id = ?";
        return jdbcTemplate.update(sql, name, new Timestamp(System.currentTimeMillis()), id);
    }

    // Elimina un usuari pel seu ID.
    public int deleteById(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Afegir una imatge de l’usuari
    public int updateImagePath(Long id, String imagePath) {
        String sql = "UPDATE user SET image_path = ? WHERE id = ?";
        return jdbcTemplate.update(sql, imagePath, id);
    }
}

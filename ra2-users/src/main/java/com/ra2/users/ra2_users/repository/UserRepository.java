package com.ra2.users.ra2_users.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static class UserRowMapper implements RowMapper<User> {
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
            return user; // Retornem l’objecte User creat
        }
    }

    // Adherir nous users. Els valors dataCreated i dataUpdated s’estableixen automàticament amb NOW().
    public int save(User user) {
        String sql = "INSERT INTO user (name, description, email, password, ultimAcces, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?, now(), now())"; // JDBC remplaça cada "?" al SQL per un valor corresponenet en ordre. poso ? en 5 posicions i dos now() fices (q no son placeholders, són funcions de SQL executades per Mysql) Només existeixen 5 placeholders reals!
        return jdbcTemplate.update(sql, user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), user.getUltimAcces());  //si el numero de ? i valors no coincideix JDBC llança Error "Parameter index out of range". Només posos 5 placeholder i MYSQL gestionarà adequadament
    }

    // Retorna tots els usuaris en format llista d'Users
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, new UserRowMapper()); // query() executa el SELECT i utilitza el RowMapper per convertir cada fila a un User
    }

    // Busca un usuari pel seu ID
    public User findOne(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id); // queryForObject() espera un únic resultat; si no hi ha cap fila, llença excepció
    }

    // Actualitza totes les dades d’un usuari existent. Només actualitza els camps: name, description, email, password i dataUpdated.
    public int modifyUser(User user, Long id) {
        String sql = "UPDATE user SET name = ?, description = ?, email = ?, password = ?, dataUpdated = now() WHERE id = ?";
        return jdbcTemplate.update(sql, user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), id);
    }

    // Elimina un usuari pel seu ID.
    public int deleteById(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

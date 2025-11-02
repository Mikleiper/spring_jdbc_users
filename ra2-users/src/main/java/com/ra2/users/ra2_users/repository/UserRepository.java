package com.ra2.users.ra2_users.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra2.users.ra2_users.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Adherir nous users
    public int crearUsuari(User user) {
        String sql = """
                INSERT INTO user (name, description, email, password, dataCreated, dataUpdated)
                VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        LocalDateTime ara = LocalDateTime.now();
        user.setDataCreated(Timestamp.valueOf(ara));
        user.setDataUpdated(Timestamp.valueOf(ara));

        return jdbcTemplate.update(sql,
                user.getName(),
                user.getDescription(),
                user.getEmail(),
                user.getPassword(),
                user.getDataCreated(),
                user.getDataUpdated()
        );
    }

    //Consultar tots els usuaris
    public List<User> findAll() {
        String sql = "SELECT * FROM user";

        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public int deleteById(Long id) {
    String sql = "DELETE FROM user WHERE id = ?";
    return jdbcTemplate.update(sql, id);
    }

    //
    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException{
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setDescription(rs.getString("description"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUltimAcces(rs.getTimestamp("ultimacces"));
            user.setDataCreated(rs.getTimestamp("dataCreated"));
            user.setDataUpdated(rs.getTimestamp("dataUpdated"));
            return user;
        }
    }


}

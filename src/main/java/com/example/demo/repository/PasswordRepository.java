package com.example.demo.repository;

import com.example.demo.model.Password;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class PasswordRepository {
    
    private final JdbcTemplate jdbc;

    public PasswordRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    } 

    public Password save(Password password) {
        String sql = """
                INSERT INTO passwords (id, user_id, title, secret, created_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        jdbc.update(
            sql,
            password.getId(),
            password.getUserId(),
            password.getTitle(),
            password.getSecret(),
            Timestamp.from(password.getCreatedAt())
        );

        return password;
    }
}

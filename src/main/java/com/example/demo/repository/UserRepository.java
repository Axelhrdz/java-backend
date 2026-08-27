package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT id, name, email, password FROM users WHERE email = ?";

        List<User> rows = jdbc.query(
            sql,
            (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password")
            ),
            email
        );

        return rows.stream().findFirst();
    }

    public User save(User user) {
        String sql = """
            INSERT INTO users (id, name, email, password)
            VALUES (?, ?, ?, ?)
            """;

        jdbc.update(
            sql,
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword()
        );

        return user;
    }
}

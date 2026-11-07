package com.example.demo.repository;

import com.example.demo.model.Password;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// import com.example.demo.dto.PasswordVaultResponse;
// import com.example.demo.model.Password;
// import com.example.demo.dto.PasswordVaultRow;
import com.example.demo.dto.EncryptedVaultRow;

// import com.example.demo.service.VaultEncryptionService;

import java.sql.Timestamp;
import java.util.List;
// import java.util.Optional;

@Repository
public class PasswordRepository {
    private final JdbcTemplate jdbc;

    public PasswordRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    } 

    public boolean existsByUserIdAndTitle(String userId, String title) {
        String sql = """
            SELECT EXISTS (
                SELECT 1
                FROM passwords
                WHERE user_id = ? AND title = ?
            )
        """;

        return Boolean.TRUE.equals(
            jdbc.queryForObject(sql, Boolean.class, userId, title)
        );
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


    //get all passwords per userId
    public List<EncryptedVaultRow> findAllByUserId(String userId) {
        String sql = """
                SELECT id, title, secret, user_id
                FROM passwords
                WHERE user_id = ?
                """;

        List<EncryptedVaultRow> res = jdbc.query(
            sql,
            (rs, rowNum) -> new EncryptedVaultRow(
                rs.getString("id"),
                rs.getString("title"),
                rs.getString("secret"),
                rs.getString("user_id")
            ),
            userId
        );


        return res;
    }
}

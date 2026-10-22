package com.example.demo.repository;

import com.example.demo.model.Password;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// import com.example.demo.dto.PasswordVaultResponse;
// import com.example.demo.model.Password;
import com.example.demo.dto.PasswordVaultRow;

import com.example.demo.service.VaultEncryptionService;

import java.sql.Timestamp;
import java.util.List;
// import java.util.Optional;

@Repository
public class PasswordRepository {
    
    private final JdbcTemplate jdbc;
    private final VaultEncryptionService vaultEncryptionService;

    public PasswordRepository(JdbcTemplate jdbc, VaultEncryptionService vaultEncryptionService) {
        this.jdbc = jdbc;
        this.vaultEncryptionService = vaultEncryptionService;
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
    public List<PasswordVaultRow> findAllByUserId(String userId) {
        String sql = """
                SELECT id, title, secret, user_id
                FROM passwords
                WHERE user_id = ?
                """;

        List<PasswordVaultRow> res = jdbc.query(
            sql,
            (rs, rowNum) -> new PasswordVaultRow(
                rs.getString("id"),
                rs.getString("title"),
                vaultEncryptionService.decrypt(rs.getString("secret"), rs.getString("user_id"), rs.getString("id")),
                rs.getString("user_id")
            ),
            userId
        );


        return res;
    }
}

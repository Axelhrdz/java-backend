package com.example.demo.repository;

import com.example.demo.model.User;
// import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// import org.springframework.stereotype.Component;
// import org.springframework.stereotype.Repository;

// import java.util.Map;
// import java.util.HashMap;
import java.util.Optional;
import java.util.List;


// public interface UserRepository extends JpaRepository<User, String> {
//     Optional<User> findByEmail(String email);
// }
// public class UserRepository {
//     private final Map<String, User> usersByEmail = new HashMap<>();

//     public Optional<User> findByEmail(String email) {
//         return Optional.ofNullable(usersByEmail.get(email));
//     }

//     public User save(User user) {
//         usersByEmail.put(user.getEmail(), user);
//         return user;
//     }
// }

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

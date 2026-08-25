package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// import org.springframework.stereotype.Component;
// import org.springframework.stereotype.Repository;

// import java.util.Map;
// import java.util.HashMap;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
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

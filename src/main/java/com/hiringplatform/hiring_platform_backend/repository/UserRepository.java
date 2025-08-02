package com.hiringplatform.hiring_platform_backend.repository;

import com.hiringplatform.hiring_platform_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A Spring Data MongoDB repository for managing User entities.
 * This interface provides a high-level abstraction for database operations
 * related to user accounts, including standard CRUD functionality.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Defines a custom query method to find a user by their unique username.
     * This method is critical for the authentication process, as Spring Security's
     * UserDetailsService will use it to fetch user details during login.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the User if found, otherwise an empty Optional.
     */
    Optional<User> findByUsername(String username);

}

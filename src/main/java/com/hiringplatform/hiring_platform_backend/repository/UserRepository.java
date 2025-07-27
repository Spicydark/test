package com.hiringplatform.hiring_platform_backend.repository;

import com.hiringplatform.hiring_platform_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * This method is essential for the login process.
     * Spring Security will use it to look up a user by their username.
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, or an empty Optional otherwise.
     */
    Optional<User> findByUsername(String username);

}

package com.hiringplatform.hiring_platform_backend.repository;

import com.hiringplatform.hiring_platform_backend.model.CandidateProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A Spring Data MongoDB repository for managing CandidateProfile entities.
 * This interface provides a high-level abstraction for database operations,
 * including standard CRUD functionality inherited from MongoRepository.
 */
@Repository
public interface CandidateProfileRepository extends MongoRepository<CandidateProfile, String> {

    /**
     * Defines a custom query method to find a candidate's profile by their associated user ID.
     * This is crucial for linking a candidate's professional details to their login account.
     *
     * @param userId The unique ID of the user from the 'users' collection.
     * @return An Optional containing the CandidateProfile if one is found for the given userId,
     * otherwise an empty Optional.
     */
    Optional<CandidateProfile> findByUserId(String userId);

}

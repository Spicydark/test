package com.hiringplatform.hiring_platform_backend.repository;

import com.hiringplatform.hiring_platform_backend.model.CandidateProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This repository interface manages the CandidateProfile collection in MongoDB.
 * It provides standard CRUD (Create, Read, Update, Delete) operations out of the box.
 */
@Repository
public interface CandidateProfileRepository extends MongoRepository<CandidateProfile, String> {

    /**
     * Finds a candidate's profile based on their associated user ID.
     * This will be useful for fetching a profile for the currently logged-in user.
     *
     * @param userId The unique ID of the user from the 'users' collection.
     * @return An Optional containing the CandidateProfile if found.
     */
    Optional<CandidateProfile> findByUserId(String userId);

}

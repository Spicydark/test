package com.hiringplatform.hiring_platform_backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.hiringplatform.hiring_platform_backend.model.JobPosting;

/**
 * A Spring Data MongoDB repository for managing JobPosting entities.
 * This interface provides a high-level abstraction for database operations,
 * including standard CRUD functionality inherited from MongoRepository.
 * It is used for creating, reading, updating, and deleting job posts.
 */
@Repository
public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
    // By extending MongoRepository, we get methods like save(), findAll(), findById(), etc.,
    // without needing to write any implementation code.
    // Custom query methods can be added here if needed.
}

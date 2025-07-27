package com.hiringplatform.hiring_platform_backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.hiringplatform.hiring_platform_backend.model.JobPosting;

/**
 * This interface is our "remote control" for the JobPostings collection in MongoDB.
 * By extending MongoRepository, we get a lot of powerful database methods for free,
 * like save(), findAll(), findById(), delete(), etc.
 */
@Repository // Marks this as a Spring Data repository component
public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
}

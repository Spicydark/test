package com.hiringplatform.hiring_platform_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hiringplatform.hiring_platform_backend.model.JobPosting;
import com.hiringplatform.hiring_platform_backend.repository.JobPostingRepository;
import com.hiringplatform.hiring_platform_backend.repository.SearchRepository;

import java.util.List;

/**
 * This class is the Controller for our Job Posting operations.
 * It exposes REST endpoints that clients can call to interact with job postings.
 */
@RestController // Combination of @Controller and @ResponseBody. It makes this class ready for REST APIs.
@RequestMapping("/posts") // All requests to this controller will start with /posts (e.g., http://localhost:8080/posts)
public class PostController {

    // Spring will automatically inject an instance of JobPostingRepository here.
    // This is called Dependency Injection.
    @Autowired
    private JobPostingRepository jobPostingRepository;
    
    @Autowired
    private SearchRepository srepo;

    /**
     * Endpoint to get all job postings from the database.
     * It handles GET requests to /posts/all
     * @return A list of all JobPosting objects.
     */
    @GetMapping("/all")
    public List<JobPosting> getAllPosts() {
        return jobPostingRepository.findAll();
    }

    /**
     * Endpoint to add a new job posting.
     * It handles POST requests to /posts/add
     * The job data is expected in the request's body as JSON.
     * @param post The JobPosting object, created from the incoming JSON.
     * @return The saved JobPosting object, including its new unique ID.
     */
    @PostMapping("/add")
    public JobPosting addPost(@RequestBody JobPosting post) {
        // The save method will either insert a new document or update an existing one.
        return jobPostingRepository.save(post);
    }
    
    @GetMapping("/search/{text}")
    public List<JobPosting> search(@PathVariable String text) {
        // This calls the findByText method we just added to the repository.
        return srepo.findByText(text);
    }
}

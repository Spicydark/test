package com.hiringplatform.hiring_platform_backend.controller;

import com.hiringplatform.hiring_platform_backend.model.CandidateProfile;
import com.hiringplatform.hiring_platform_backend.model.JobPosting;
import com.hiringplatform.hiring_platform_backend.model.User;
import com.hiringplatform.hiring_platform_backend.repository.CandidateProfileRepository;
import com.hiringplatform.hiring_platform_backend.repository.JobPostingRepository;
import com.hiringplatform.hiring_platform_backend.repository.SearchRepository;
import com.hiringplatform.hiring_platform_backend.repository.UserRepository;
import com.hiringplatform.hiring_platform_backend.service.EmailService; // <-- IMPORT EMAIL SERVICE
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private JobPostingRepository jobPostingRepository;
    
    @Autowired
    private SearchRepository srepo;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CandidateProfileRepository candidateProfileRepository; // <-- Inject Candidate Repo

    @Autowired
    private EmailService emailService; // <-- Inject Email Service

    // ... (existing getAllPosts, addPost, and search methods remain the same)
    @GetMapping("/all")
    public List<JobPosting> getAllPosts() { return jobPostingRepository.findAll(); }
    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody JobPosting post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Optional<User> userOptional = userRepository.findByUsername(currentUsername);
        if (userOptional.isEmpty()) { return ResponseEntity.status(404).body("Recruiter user not found!"); }
        post.setRecruiterId(userOptional.get().getId());
        JobPosting savedPost = jobPostingRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }
    @GetMapping("/search/{text}")
    public List<JobPosting> search(@PathVariable String text) { return srepo.findByText(text); }


    /**
     * Handles POST requests for a job seeker to apply for a job.
     * Triggers an email notification to the recruiter who posted the job.
     *
     * @param jobId The ID of the job being applied for.
     * @return A success or error message.
     */
    @PostMapping("/apply/{jobId}")
    public ResponseEntity<String> applyForJob(@PathVariable String jobId) {
        // 1. Get the currently authenticated user (the candidate)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String candidateUsername = authentication.getName();
        Optional<User> candidateUserOptional = userRepository.findByUsername(candidateUsername);
        if (candidateUserOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Applying user not found.");
        }
        User candidateUser = candidateUserOptional.get();

        // 2. Get the candidate's profile
        Optional<CandidateProfile> candidateProfileOptional = candidateProfileRepository.findByUserId(candidateUser.getId());
        if (candidateProfileOptional.isEmpty()) {
            return ResponseEntity.status(400).body("Please create your profile before applying.");
        }
        CandidateProfile candidateProfile = candidateProfileOptional.get();

        // 3. Find the job posting
        Optional<JobPosting> jobOptional = jobPostingRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Job not found.");
        }
        JobPosting job = jobOptional.get();

        // 4. Find the recruiter's details
        Optional<User> recruiterUserOptional = userRepository.findById(job.getRecruiterId());
        if (recruiterUserOptional.isEmpty()) {
            return ResponseEntity.status(500).body("Could not find the recruiter for this job.");
        }
        User recruiter = recruiterUserOptional.get();

        // 5. Send the email
        String subject = "New Application for " + job.getRole();
        String body = "Hello,\n\nA new candidate has applied for the '" + job.getRole() + "' position.\n\n"
                    + "Candidate Name: " + candidateProfile.getFullName() + "\n"
                    + "Candidate Email: " + candidateProfile.getEmail() + "\n"
                    + "Candidate Experience: " + candidateProfile.getTotalExperience() + " years\n"
                    + "Candidate Skills: " + String.join(", ", candidateProfile.getSkills()) + "\n\n"
                    + "You can view their resume here: " + candidateProfile.getResumeUrl() + "\n\n"
                    + "Thank you,\nThe Hiring Platform";
        
        emailService.sendEmail(recruiter.getEmail(), subject, body);

        return ResponseEntity.ok("Application submitted successfully!");
    }
}
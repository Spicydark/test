package com.hiringplatform.hiring_platform_backend.controller;

import com.hiringplatform.hiring_platform_backend.model.CandidateProfile;
import com.hiringplatform.hiring_platform_backend.model.User;
import com.hiringplatform.hiring_platform_backend.repository.CandidateProfileRepository;
import com.hiringplatform.hiring_platform_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller responsible for handling API endpoints related to candidate profiles.
 */
@RestController
@RequestMapping("/candidate")
public class CandidateController {

    /**
     * Injected repository for candidate profile data access.
     */
    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    /**
     * Injected repository for user data access, needed to link profiles to users.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Handles POST requests to create or update a candidate's profile.
     * This endpoint is secured and ensures that only a logged-in JOB_SEEKER
     * can modify their own profile.
     *
     * @param profile The candidate profile data sent in the request body.
     * @return A ResponseEntity containing the saved profile or an error message.
     */
    @PostMapping("/profile")
    public ResponseEntity<?> saveOrUpdateProfile(@RequestBody CandidateProfile profile) {
        // Retrieve the currently authenticated user's details from the security context.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Fetch the full user object from the database to get their ID and role.
        Optional<User> userOptional = userRepository.findByUsername(currentUsername);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("Authenticated user not found in the database!", HttpStatus.NOT_FOUND);
        }
        User currentUser = userOptional.get();

        // Although SecurityConfig provides the primary layer of role-based security,
        // this check adds a secondary, explicit verification.
        if (!"JOB_SEEKER".equals(currentUser.getRole())) {
             return new ResponseEntity<>("Access Denied: This action is reserved for JOB_SEEKER role.", HttpStatus.FORBIDDEN);
        }

        // Associate this profile with the currently logged-in user by setting the userId.
        // This prevents a user from creating a profile for someone else.
        profile.setUserId(currentUser.getId());
        CandidateProfile savedProfile = candidateProfileRepository.save(profile);

        return new ResponseEntity<>(savedProfile, HttpStatus.OK);
    }

    /**
     * Handles GET requests to retrieve a candidate's profile using their user ID.
     * This endpoint is accessible to any authenticated user (recruiters or job seekers).
     *
     * @param userId The unique ID of the user whose profile is being requested.
     * @return A ResponseEntity containing the found profile or a 404 Not Found error.
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfileByUserId(@PathVariable String userId) {
        Optional<CandidateProfile> profileOptional = candidateProfileRepository.findByUserId(userId);

        if (profileOptional.isEmpty()) {
            return new ResponseEntity<>("Profile not found for the specified user.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(profileOptional.get(), HttpStatus.OK);
    }
}

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

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint for a logged-in JOB_SEEKER to create or update their profile.
     * It links the profile to their user account.
     * @param profile The candidate profile data from the request body.
     * @return The saved candidate profile.
     */
    @PostMapping("/profile")
    public ResponseEntity<?> saveOrUpdateProfile(@RequestBody CandidateProfile profile) {
        // Get the username of the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Find the user in the database
        Optional<User> userOptional = userRepository.findByUsername(currentUsername);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
        }
        User currentUser = userOptional.get();

        // This check is a good second layer, but the primary check should be in SecurityConfig.
        if (!"JOB_SEEKER".equals(currentUser.getRole())) {
             return new ResponseEntity<>("Access Denied: Only JOB_SEEKER can create a profile.", HttpStatus.FORBIDDEN);
        }

        // Set the userId on the profile to link it to the user account
        profile.setUserId(currentUser.getId());
        CandidateProfile savedProfile = candidateProfileRepository.save(profile);
        return new ResponseEntity<>(savedProfile, HttpStatus.OK);
    }

    /**
     * Endpoint to get a candidate's profile by their user ID.
     * @param userId The ID of the user whose profile is being requested.
     * @return The candidate's profile if found.
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfileByUserId(@PathVariable String userId) {
        Optional<CandidateProfile> profileOptional = candidateProfileRepository.findByUserId(userId);
        if (profileOptional.isEmpty()) {
            return new ResponseEntity<>("Profile not found for this user.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(profileOptional.get(), HttpStatus.OK);
    }
}
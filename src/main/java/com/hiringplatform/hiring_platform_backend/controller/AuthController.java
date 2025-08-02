package com.hiringplatform.hiring_platform_backend.controller;

import com.hiringplatform.hiring_platform_backend.dto.AuthRequest;
import com.hiringplatform.hiring_platform_backend.model.User;
import com.hiringplatform.hiring_platform_backend.repository.UserRepository;
import com.hiringplatform.hiring_platform_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling authentication-related endpoints,
 * such as user registration and login.
 */
@RestController
public class AuthController {

    /**
     * Injected repository for user data access.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Injected encoder for hashing passwords.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Injected manager to handle the authentication process.
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Injected utility for creating and managing JSON Web Tokens.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Handles POST requests to the /register endpoint.
     * Creates a new user account, hashes the password, and saves it to the database.
     *
     * @param user The user details (username, password, role) from the request body.
     * @return A success message or an error if the username is already taken.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Prevent duplicate usernames by checking if the user already exists.
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Securely hash the user's password before saving it to the database.
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    /**
     * Handles POST requests to the /login endpoint.
     * Authenticates the user's credentials and returns a JWT upon success.
     *
     * @param authRequest A DTO containing the user's username and password.
     * @return A ResponseEntity containing the JWT string or an error message.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthRequest authRequest) {
        try {
            // The AuthenticationManager validates the credentials using the configured AuthenticationProvider.
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            
            // If authentication is successful, retrieve the authenticated user's details.
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate a JWT for the authenticated user.
            final String jwt = jwtUtil.generateToken(userDetails);
            
            // Return the JWT to the client.
            return ResponseEntity.ok(jwt);

        } catch (Exception e) {
            // If authentication fails (e.g., bad credentials), return a 401 Unauthorized status.
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}

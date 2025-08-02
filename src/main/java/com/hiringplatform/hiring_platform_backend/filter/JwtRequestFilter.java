package com.hiringplatform.hiring_platform_backend.filter;

import com.hiringplatform.hiring_platform_backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A custom Spring Security filter that intercepts every incoming request once.
 * Its primary responsibility is to inspect the request for a JWT in the
 * Authorization header, validate it, and set the user's authentication
 * details in the security context if the token is valid.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * Injected utility for handling JWT operations like extraction and validation.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Injected service for loading user data from the database.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * The core logic of the filter that is executed for each request.
     *
     * @param request The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param filterChain The chain of filters to pass the request along to.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the Authorization header from the request.
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Check if the header exists and follows the "Bearer <token>" format.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extract the token string.
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Handle cases where the token is malformed or expired.
                System.out.println("Cannot extract username from JWT or token is expired");
            }
        }

        // If a username was successfully extracted and there is no existing authentication
        // in the security context, proceed with validation.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user's details from the database.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the token against the loaded user details.
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // If the token is valid, create an authentication token.
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set the authentication in the security context.
                // This effectively "logs in" the user for the duration of the request.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Pass the request and response along to the next filter in the chain.
        filterChain.doFilter(request, response);
    }
}

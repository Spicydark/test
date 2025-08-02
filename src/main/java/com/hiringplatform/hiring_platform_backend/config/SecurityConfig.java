package com.hiringplatform.hiring_platform_backend.config;

import com.hiringplatform.hiring_platform_backend.filter.JwtRequestFilter;
import com.hiringplatform.hiring_platform_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/**
 * Main security configuration class for the application.
 * This class enables web security and configures all security aspects,
 * including authentication, authorization, password encoding, and filter chains.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserRepository userRepository;

    /**
     * Defines a service to load user-specific data from the database.
     * Spring Security uses this to fetch user details during authentication.
     * It maps a User entity from the database to Spring Security's UserDetails object.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
            .map(user -> new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(
                    // Spring Security requires roles to be prefixed with "ROLE_" for hasRole() checks.
                    new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole())
                )
            ))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    /**
     * Provides a PasswordEncoder bean to the application context.
     * Uses BCrypt, a strong hashing algorithm, to securely store user passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the primary authentication mechanism.
     * This provider uses the custom UserDetailsService and PasswordEncoder to validate user credentials.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exposes the AuthenticationManager as a bean.
     * This is the core component that processes an authentication request.
     * It's injected into the AuthController to handle the /login endpoint.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the main security filter chain that applies to all incoming HTTP requests.
     * This is where all authorization rules, session management, and custom filters are defined.
     *
     * @param http The HttpSecurity object to configure.
     * @param jwtAuthFilter The custom filter for processing JWTs.
     * @return The configured SecurityFilterChain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtRequestFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/register", "/login").permitAll()
                .requestMatchers("/posts/search/**", "/posts/all").permitAll()

                // Role-Based Authorization
                .requestMatchers(HttpMethod.POST, "/posts/add").hasRole("RECRUITER")
                .requestMatchers(HttpMethod.POST, "/candidate/profile").hasRole("JOB_SEEKER")
                .requestMatchers(HttpMethod.GET, "/candidate/profile/**").authenticated()
                
                // --- NEW RULE FOR APPLYING ---
                .requestMatchers(HttpMethod.POST, "/posts/apply/**").hasRole("JOB_SEEKER")

                // Catch-all rule
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
            );

        return http.build();
    }

    /**
     * Defines the CORS configuration for the application.
     * This is essential for allowing frontend applications from different origins to access the API.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // In production, this should be restricted to the specific frontend domain.
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Defines the behavior for when an unauthenticated user tries to access a protected resource.
     * Returns a 401 Unauthorized status.
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.getWriter().write("Unauthorized: " + authException.getMessage());
        };
    }

    /**
     * Defines the behavior for when an authenticated user tries to access a resource they do not have permission for.
     * Returns a 403 Forbidden status.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(403);
            response.getWriter().write("Access Denied: " + accessDeniedException.getMessage());
        };
    }
    
    
}

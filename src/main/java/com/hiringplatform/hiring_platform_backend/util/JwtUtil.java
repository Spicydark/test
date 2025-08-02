package com.hiringplatform.hiring_platform_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A utility class for handling JSON Web Token (JWT) operations.
 * This includes generating, parsing, and validating tokens.
 */
@Component
public class JwtUtil {

    /**
     * A secure, static secret key for signing and verifying JWTs.
     * Uses HMAC-SHA algorithm. In a production environment, this key should be
     * externalized into application properties or a secure vault.
     */
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("hiringplatformsecretkey12345678901234567890".getBytes());

    /**
     * Extracts the username (subject) from a given JWT.
     *
     * @param token The JWT string.
     * @return The username contained within the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from a given JWT.
     *
     * @param token The JWT string.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * A generic method to extract a specific claim from a token.
     *
     * @param token The JWT string.
     * @param claimsResolver A function to resolve the desired claim from the claims body.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT and returns all claims contained within its body.
     *
     * @param token The JWT string.
     * @return The Claims object representing the token's payload.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if a given JWT has expired.
     *
     * @param token The JWT string.
     * @return True if the token has expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a new JWT for a given user.
     *
     * @param userDetails The user details for whom the token is being generated.
     * @return A new, signed JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Custom claims can be added here (e.g., roles, user ID)
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Helper method to construct the JWT with its claims, subject, issuance date,
     * expiration date, and signature.
     *
     * @param claims A map of custom claims to include in the token.
     * @param subject The subject of the token (typically the username).
     * @return The compacted, URL-safe JWT string.
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token is valid for 10 hours
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT by checking if the username matches and if the token has not expired.
     *
     * @param token The JWT string to validate.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

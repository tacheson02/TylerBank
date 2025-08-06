package tylerbank.web.app.TylerBank.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;

/**
 * Class to create, parse, and validate JSON web token (JWT token).
 * Token used to quickly and securely verify user authentication, reduces database lookups.
 * @since v1.1
 */
@Service
public class JwtService {

    // Expiration time for the JWT token in milliseconds (24 hours)
    private static final long EXPIRATION_TIME = 86400000;

    @Value("${jwtSecret}")
    private String jwtSecret;

    /**
     * Converts jwtSecret into a SecretKey object using HMAC-SHA algorithms
     * @return SecretKey object
     * @since v1.1
     */
    public SecretKey generateKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Create new token with given username and expiration time
     * @param username
     * @return String, token
     * @Since v1.1
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(generateKey())
                .compact();
    }

    /**
     * Extract the claims (payload) from the token.
     * Uses jjwt library parser [Jwts.parser()]
     * Verifies token signature [.verifyWith(generateKey())]
     * Parses token [.build() and.parseSignedClaims(token)]
     * Returns Claims object with the token's payload [.getPayload()]
     * @param token
     * @return Claims, from token
     * @since v1.1
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract the username from the token's claims
     * @param token
     * @return String, subject from token (username)
     * @since v1.1
     */
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Check if the token is expired
     * @param token
     * @return boolean, true if token is expired, false otherwise
     * @since v1.1
     */
    public boolean isTokenValid(String token) {
        return new Date().before(extractExpiration(token));
    }

    /**
     * Extract the expiration time from the token's claims
     * @param token
     * @return Date, from token
     * @since v1.1
     */
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }
}

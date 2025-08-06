package tylerbank.web.app.TylerBank.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;

/* Class to create, parse, and validate JSON web token (JWT token).
 * Token used to quickly and securely verify user authentication, reduces database lookups.
 */
@Service
public class JwtService {

    // Expiration time for the JWT token in milliseconds (24 hours)
    private static final long EXPIRATION_TIME = 86400000;

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Converts jwtSecret into a SecretKey object using HMAC-SHA algorithms
    public SecretKey generateKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Create new token with given username and expiration time
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

    /* Extract the claims (payload) from the token.
     * Uses jjwt library parser [Jwts.parser()]
     * Verifies token signature [.verifyWith(generateKey())]
     * Parses token [.build() and.parseSignedClaims(token)]
     * Returns Claims object with the token's payload [.getPayload()]
     */
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract the username from the token's claims
    public String extractSubject(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if the token is expired
    public boolean isTokenValid(String token) {
        return new Date().before(extractExpiration(token));
    }

    // Extract the expiration time from the token's claims
    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }
}

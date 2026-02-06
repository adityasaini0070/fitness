package com.project.fitness.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(String userId, String role) {
        int jwtExpirationMs = 7280000;

        List<Map<String, String>> roles = Collections.singletonList(
                Collections.singletonMap("authority", role));

        return Jwts.builder()
                .subject(userId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser().verifyWith(key()).build()
                    .parseSignedClaims(jwtToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private SecretKey key() {
        String jwtSecret = "MmU0OTY5ZDYtMzRlNy00ZTM4LWExNjQtZjYwYTkzYzY3N2RkMjNiZDcyYjUtZTJhOS00Yjk4LThhMTgtYmZkNGRjZTIyOTYy";
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String jwt) {
        return Jwts.parser().verifyWith(key())
                .build().parseSignedClaims(jwt)
                .getPayload().getSubject();
    }
}

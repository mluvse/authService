package by.mluvse.authService.security.jwt;

import by.mluvse.authService.dto.TokenResponse;
import by.mluvse.authService.entity.Credentials;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Component
public class JwtService {

    private static final Logger logger=  LoggerFactory.getLogger(JwtService.class);

    @Value(value = "67ZoSbcCqqRaJTmV2gIVwz7Wn0B0QiUx2ARBLR4S9Zw")
    private String jwtSecret;

    public TokenResponse generateAuthToken(Credentials credentials){
        return new TokenResponse(generateJwtToken(credentials),generateRefreshToken(credentials));
    }

    public TokenResponse refreshBaseToken(Credentials credentials, String refreshToken){
        return new TokenResponse(generateJwtToken(credentials),refreshToken);
    }

    public String getLoginFromToken(String token){
        Claims claims= Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException exception){
            logger.warn("Expired JwtException", exception);
        } catch (UnsupportedJwtException exception){
            logger.warn("Unsupported JwtException", exception);
        }catch (MalformedJwtException exception){
            logger.warn("Malformed JwtException", exception);
        }catch (SecurityException exception){
            logger.warn("Security Exception", exception);
        }catch (Exception exception){
            logger.warn("Invalid token", exception);
        }
        return false;
    }
    private String generateJwtToken(Credentials credentials) {
        Date date= Date.from(LocalDateTime.now().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(credentials.getLogin())
                .claim("userId",credentials.getUserId())
                .claim("role", credentials.getRole().name())
                .issuedAt(new Date())
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }


    private String generateRefreshToken(Credentials credentials){
        Date date= Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(credentials.getLogin())
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private SecretKey getSingInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("userId", Long.class);
    }


    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("role", String.class);
    }
}

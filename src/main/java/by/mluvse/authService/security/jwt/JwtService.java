package by.mluvse.authService.security.jwt;

import by.mluvse.authService.dto.JwtAuthDto;
import by.mluvse.authService.entity.Credentials;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;



@Component
public class JwtService {

    private static final Logger logger= LogManager.getLogger(JwtService.class);

    @Value("UwZlZigh67vLBS6ZN4yJUABoCIvb7Zh7eHLZmuLcpmG")
    private String jwtSecret;

    public JwtAuthDto generateAuthToken(Credentials credentials){
        JwtAuthDto jwtAuthDto= new JwtAuthDto();
        jwtAuthDto.setToken(generateJwtToken(credentials));
        jwtAuthDto.setRefreshToken(generateRefreshToken(credentials));
        return jwtAuthDto;
    }

    public JwtAuthDto refreshBaseToken(Credentials credentials, String refreshToken){
        JwtAuthDto jwtAuthDto= new JwtAuthDto();
        jwtAuthDto.setToken(generateJwtToken(credentials));
        jwtAuthDto.setRefreshToken(refreshToken);
        return jwtAuthDto;
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
            logger.error("Expired JwtException", exception);
        } catch (UnsupportedJwtException exception){
            logger.error("Unsupported JwtException", exception);
        }catch (MalformedJwtException exception){
            logger.error("Malformed JwtException", exception);
        }catch (SecurityException exception){
            logger.error("Security Exception", exception);
        }catch (Exception exception){
            logger.error("Invalid token", exception);
        }
        return false;
    }
    private String generateJwtToken(Credentials credentials) {
        Date date= Date.from(LocalDateTime.now().plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .subject(credentials.getLogin())
                .claim("userId",credentials.getUserId())
                .claim("role", credentials.getRole())
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
}

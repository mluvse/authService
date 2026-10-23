package by.mluvse.authService.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.security.core.AuthenticationException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalig login or password"+ exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUserNotFound(UsernameNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"+exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDenied(AccessDeniedException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied"+ exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleException(AuthenticationException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication failed"+exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred"+exception.getMessage());
    }
}

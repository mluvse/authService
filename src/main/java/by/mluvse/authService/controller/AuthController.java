package by.mluvse.authService.controller;

import by.mluvse.authService.dto.*;
import by.mluvse.authService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest request){
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public  TokenResponse refresh(@RequestBody @Valid RefreshRequest request){
        return authService.refresh(request);
    }

    @PostMapping("/validate")
    public  ValidateResponse validate(@RequestBody @Valid ValidateRequest request){
        return  authService.validate(request);
    }
}

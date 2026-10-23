package by.mluvse.authService.service;


import by.mluvse.authService.Role;
import by.mluvse.authService.dto.*;
import by.mluvse.authService.entity.Credentials;
import by.mluvse.authService.repository.CredentialsRepository;
import by.mluvse.authService.security.CustomUserDetails;
import by.mluvse.authService.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Credentials credentials = userDetails.getCredentials();

        return jwtService.generateAuthToken(credentials);
    }

    public void register(RegisterRequest request) {
        if (credentialsRepository.existsByLogin(request.login())) {
            throw new IllegalArgumentException("Login already exists");
        }

        Credentials credentials = new Credentials();
        credentials.setLogin(request.login());
        credentials.setPasswordHash(passwordEncoder.encode(request.password()));
        credentials.setUserId(request.userId());
        credentials.setRole(Role.USER);
        credentials.setActive(true);

        credentialsRepository.save(credentials);
    }

    public TokenResponse refresh(RefreshRequest request) {
        String login = jwtService.getLoginFromToken(request.refreshToken());
        Credentials credentials = credentialsRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return jwtService.refreshBaseToken(credentials, request.refreshToken());
    }

    public ValidateResponse validate(ValidateRequest request) {
        boolean isValid = jwtService.validateJwtToken(request.token());
        if (isValid) {
            Long userId = jwtService.getUserIdFromToken(request.token());
            String role = jwtService.getRoleFromToken(request.token());
            return new ValidateResponse(true, userId, role);
        }
        return new ValidateResponse(false, null, null);
    }
}

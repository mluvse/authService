package by.mluvse.authService.dto;

public record ValidateResponse(boolean valid, Long userId, String role) {
}

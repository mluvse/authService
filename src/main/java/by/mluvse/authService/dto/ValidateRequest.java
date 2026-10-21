package by.mluvse.authService.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidateRequest(@NotBlank String token) {
}

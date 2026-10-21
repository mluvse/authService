package by.mluvse.authService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank String login,
                              @NotBlank @Size(min = 8) String password,
                              @NotNull Long userId) {
}

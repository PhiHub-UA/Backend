package deti.tqs.phihub.DTOs;

import jakarta.validation.constraints.NotNull;

public record registerSchema(@NotNull String name, @NotNull String phone, @NotNull String email, @NotNull Integer age,
                @NotNull String username, @NotNull String password,
                @NotNull String role) {
}


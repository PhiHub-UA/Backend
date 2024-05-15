package deti.tqs.phihub.dtos;

import jakarta.validation.constraints.NotNull;

public record RegisterSchema(@NotNull String phone, @NotNull String email,
                @NotNull Integer age, @NotNull String username, @NotNull String password,
                @NotNull String role, @NotNull String name) {
}

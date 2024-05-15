package deti.tqs.phihub.dtos;

import deti.tqs.phihub.configs.Generated;
import jakarta.validation.constraints.NotNull;

@Generated
public record RegisterSchema(@NotNull String phone, @NotNull String email,
                @NotNull Integer age, @NotNull String username, @NotNull String password,
                @NotNull String role, @NotNull String name) {
}

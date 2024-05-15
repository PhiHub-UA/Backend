package deti.tqs.phihub.dtos;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record MedicSchema(
        @NotNull String phone, @NotNull String email,
        @NotNull Integer age, @NotNull String username, @NotNull String password,
        @NotNull String role, @NotNull String name, @Valid List<String> specialities) {
}

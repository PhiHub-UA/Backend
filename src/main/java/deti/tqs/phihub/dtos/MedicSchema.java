package deti.tqs.phihub.dtos;

import java.util.List;

import jakarta.validation.Valid;
import deti.tqs.phihub.configs.Generated;
import jakarta.validation.constraints.NotNull;

@Generated
public record MedicSchema(
        @NotNull String phone, @NotNull String email,
        @NotNull Integer age, @NotNull String username, @NotNull String password,
        @NotNull String role, @NotNull String name, @Valid List<String> specialities) {
}

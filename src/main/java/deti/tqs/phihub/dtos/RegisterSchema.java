package deti.tqs.phihub.dtos;

import deti.tqs.phihub.configs.Generated;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Generated
public record RegisterSchema(@NotNull String phone, @NotNull String email,
                @NotNull Integer age, @NotNull String username, @NotNull String password,
                @NotNull String role, @NotNull String name,
                @Nullable List<String> permissions
                ) {
}

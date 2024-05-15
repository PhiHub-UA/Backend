package deti.tqs.phihub.dtos;

import deti.tqs.phihub.configs.Generated;
import jakarta.validation.constraints.NotBlank;

@Generated
public record LoginSchema(
                @NotBlank String username,
                @NotBlank String password,
                @NotBlank String role) {
}

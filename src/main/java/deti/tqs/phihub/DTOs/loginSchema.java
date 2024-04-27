package deti.tqs.phihub.DTOs;

import jakarta.validation.constraints.NotBlank;


public record loginSchema(
        @NotBlank String username,
        @NotBlank String password
        ) {}


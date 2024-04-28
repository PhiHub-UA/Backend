package deti.tqs.phihub.dtos;

import jakarta.validation.constraints.NotBlank;


public record LoginSchema(
        @NotBlank String username,
        @NotBlank String password
        ) {}


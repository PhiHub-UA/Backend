package deti.tqs.phihub.DTOs;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record registerSchema(@NotNull String name, @NotNull String phone, @NotNull String email, @NotNull Integer age,
                @NotNull String username, @NotNull String password,
                @NotNull String role) {
}

/*
 * // Personal Info
 * private String name;
 * private String phone;
 * private String email;
 * private Integer age;
 * // Login Info
 * private String username;
 * private String password;
 * // Role Info
 * private List<String> roles;
 */

package deti.tqs.phihub.dtos;

import jakarta.validation.Valid;

public record StaffSchema(
        @Valid String phone,
        @Valid String email,
        @Valid Integer age,
        @Valid String username,
        @Valid String name,
        @Valid String password) {

}
package deti.tqs.phihub.dtos;

import deti.tqs.phihub.configs.Generated;
import jakarta.validation.Valid;

@Generated
public record StaffSchema(
        @Valid String phone,
        @Valid String email,
        @Valid Integer age,
        @Valid String username,
        @Valid String name,
        @Valid String password) {

}
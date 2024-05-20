package deti.tqs.phihub.dtos;

import deti.tqs.phihub.configs.Generated;
import deti.tqs.phihub.models.Speciality;

@Generated
public record AppointmentSchema(Long date, Double price, Speciality speciality, Long medicID) {
}

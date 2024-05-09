package deti.tqs.phihub.dtos;

import deti.tqs.phihub.models.Speciality;

public record AppointmentSchema(Long date, Double price, Speciality speciality, Long medicID) {
    

}

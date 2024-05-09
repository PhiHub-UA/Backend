package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.Speciality;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialityService {
    
    public Speciality getSpecialityById(int id) {
        return Speciality.fromId(id);
    }

    public List<String> getSpecialities() {
        List<String> specialities = new ArrayList<>();
        for (Speciality speciality : Speciality.values()) {
            specialities.add(speciality.toString());
        }
        return specialities;

    }


}

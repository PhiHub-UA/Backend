package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.Speciality;

@Service
public class SpecialityService {


    public Speciality getSpecialityById(int id) {
        
        return Speciality.fromId(id);
    }
    
}

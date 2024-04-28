package deti.tqs.phihub.services;

import org.springframework.stereotype.Service;

import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.repositories.MedicRepository;

import java.util.List;

@Service
public class MedicService {

    private MedicRepository medicRepository;

    public MedicService(MedicRepository medicRepository) {
        this.medicRepository = medicRepository;
    }

    public Medic save(Medic medic) {
        return medicRepository.save(medic);
    }

    public Medic getMedicById(Long id) {
        return medicRepository.findById(id).orElse(null);
    }

    public List<Medic> findAll() {
        return medicRepository.findAll();
    }
    
}

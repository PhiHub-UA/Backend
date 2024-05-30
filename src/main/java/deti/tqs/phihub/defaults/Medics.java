package deti.tqs.phihub.defaults;

import org.springframework.boot.ApplicationRunner;

import deti.tqs.phihub.configs.Generated;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import deti.tqs.phihub.models.Medic;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.repositories.MedicRepository;

@Component
@Generated
public class Medics implements ApplicationRunner {

    MedicRepository medicRepository;

    public Medics(MedicRepository medicRepository) {
        this.medicRepository = medicRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (medicRepository.count() > 0) {
            return;
        }

        Medic medic1 = new Medic("Manuel Gameiro", "918453817", "manel@gmail.com", 35, "ManuelGameiro", "manel123");
        medic1.setSpecialities(List.of(Speciality.CARDIOLOGY, Speciality.DERMATOLOGY, Speciality.GYNECOLOGY));

        Medic medic2 = new Medic("João Silva", "913287317", "joao@gmail.com", 27, "JoaoSilva", "joao123");
        medic2.setSpecialities(List.of(Speciality.ENDOCRINOLOGY, Speciality.HEMATOLOGY, Speciality.NEUROLOGY));

        Medic medic3 = new Medic("Maria Alberta", "913145317", "maria@gmail.com", 45, "MariaAlberta", "maria123");
        medic3.setSpecialities(
                List.of(Speciality.PEDIATRICS, Speciality.PSYCHIATRY, Speciality.PHTHALMOLOGY, Speciality.UROLOGY));

        medicRepository.save(medic1);
        medicRepository.save(medic2);
        medicRepository.save(medic3);

    }
}

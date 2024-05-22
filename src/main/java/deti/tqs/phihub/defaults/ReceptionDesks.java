package deti.tqs.phihub.defaults;

import org.springframework.boot.ApplicationRunner;

import deti.tqs.phihub.configs.Generated;
import deti.tqs.phihub.models.ReceptionDesk;
import deti.tqs.phihub.repositories.ReceptionDeskRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;


@Component
@Generated
public class ReceptionDesks implements ApplicationRunner {


    private ReceptionDeskRepository receptionDeskRepository;

    public ReceptionDesks(ReceptionDeskRepository receptionDeskRepository) {
        this.receptionDeskRepository = receptionDeskRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (receptionDeskRepository.count() > 0) {
            return;
        }

        for (int i = 1; i <= 4; i++) {
            ReceptionDesk desk = ReceptionDesk.builder().deskNumber(i).working(true).build();
            receptionDeskRepository.save(desk);
        }

    }
}

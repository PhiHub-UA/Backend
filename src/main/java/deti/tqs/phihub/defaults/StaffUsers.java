package deti.tqs.phihub.defaults;

import org.springframework.boot.ApplicationRunner;

import deti.tqs.phihub.configs.Generated;
import deti.tqs.phihub.models.Staff;
import deti.tqs.phihub.repositories.StaffRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import deti.tqs.phihub.models.StaffPermissions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;

@Component
@Generated
public class StaffUsers implements ApplicationRunner {

    @Value("${staff.aguiar.pass}")
    private String aguiarPass;

    StaffRepository staffRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    public StaffUsers(StaffRepository staffRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.staffRepository = staffRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (staffRepository.count() > 0) {
            return;
        }

        Staff aguiar = new Staff();
        aguiar.setUsername("aguiar");
        aguiar.setPassword(bCryptPasswordEncoder.encode(aguiarPass));
        aguiar.setAge(20);
        aguiar.setEmail("aguiar@gmail.com");
        aguiar.setName("Aguiar");
        aguiar.setId(1L);
        aguiar.setPhone("9877654321");
        aguiar.setPermissions(List.of(StaffPermissions.CREATE, StaffPermissions.MANAGE, StaffPermissions.RECEPTION));

        staffRepository.save(aguiar);

    }

}

package deti.tqs.phihub.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long date; // date will be stored as a timestamp

    private Double price;

    @ManyToOne
    private User patient;

    @ManyToOne
    private Medic medic;

    private Speciality speciality;

    @OneToOne(fetch = FetchType.EAGER)
    private Bill bill;

    private String notes;

    private AppointmentState state = AppointmentState.PENDING;

    
}

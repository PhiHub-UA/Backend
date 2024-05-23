package deti.tqs.phihub.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reception_desk")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReceptionDesk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int deskNumber;

    private boolean working;

    @ManyToOne
    private Ticket servingTicket;

}

package deti.tqs.phihub.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long issueTimestamp;

    private boolean priority;

    private String ticketName;

    @ManyToOne
    @JsonIgnore
    private WaitingRoom waitingRoom;

    @ManyToOne // its a OneToOne, ManyToOne just for easier testing
    @JsonIgnore
    private Appointment appointment;

}

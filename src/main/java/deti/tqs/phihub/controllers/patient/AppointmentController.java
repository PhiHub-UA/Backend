package deti.tqs.phihub.controllers.patient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import deti.tqs.phihub.models.Appointment;

import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.MedicService;
import deti.tqs.phihub.services.QueueLineService;
import deti.tqs.phihub.services.ReceptionDeskService;
import deti.tqs.phihub.services.UserService;
import deti.tqs.phihub.services.TicketService;
import deti.tqs.phihub.dtos.AppointmentSchema;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/patient/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    private UserService userService;

    private MedicService medicService;

    private TicketService ticketService;

    private ReceptionDeskService receptionDeskService;

    private QueueLineService queueLineService;

    public AppointmentController(AppointmentService appointmentService, UserService userService,
            MedicService medicService, TicketService ticketService, ReceptionDeskService receptionDeskService,
            QueueLineService queueLineService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.medicService = medicService;
        this.ticketService = ticketService;
        this.receptionDeskService = receptionDeskService;
        this.queueLineService = queueLineService;
    }

    @Operation(summary = "Create an appointment", description = "Create an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentSchema appointmentSchema) {
        var user = userService.getUserFromContext();

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        var medic = medicService.getMedicById(appointmentSchema.medicID());
        if (medic == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medic not found");
        }

        Appointment app = new Appointment();
        app.setPatient(user);
        app.setSpeciality(appointmentSchema.speciality());
        app.setDate(appointmentSchema.date());
        app.setPrice(appointmentSchema.price());
        app.setMedic(medic);
        app.setBill(null);

        Appointment savedApp = appointmentService.save(app);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApp);
    }


    @Operation(summary = "Get appointments", description = "Get appointments of the logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments() {
        var user = userService.getUserFromContext();
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(user);
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Get appointment", description = "Get appointment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        var user = userService.getUserFromContext();
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment.getPatient().getId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to access this appointment");
        }
        return ResponseEntity.ok(appointment);
    }

    @Operation(summary = "Delete appointment", description = "Delete appointment by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        
        var user = userService.getUserFromContext();
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment.getPatient().getId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized to delete this appointment");
        }
        
        queueLineService.deleteTicketFromQueueByAppointmentID(id);
        receptionDeskService.deleteTicketsByAppointmentID(id);
        ticketService.deleteTicketsByAppointmentID(id);
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok().build();
    }
}

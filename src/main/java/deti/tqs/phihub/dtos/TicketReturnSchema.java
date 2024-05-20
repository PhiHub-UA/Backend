package deti.tqs.phihub.dtos;

public record TicketReturnSchema(Long id, Long appointmentId, String queueLineLetter, int queueLineNumber, Long waitingRoomId) {
}

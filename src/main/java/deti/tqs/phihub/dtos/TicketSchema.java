package deti.tqs.phihub.dtos;

public record TicketSchema(Long queueLineId, Long issueTimestamp, boolean priority, Long waitingRoomId,
        Long appointmentId) {
}

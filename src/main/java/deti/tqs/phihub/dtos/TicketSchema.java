package deti.tqs.phihub.dtos;

import deti.tqs.phihub.configs.Generated;
@Generated
public record TicketSchema(Long queueLineId, Long issueTimestamp, boolean priority, Long waitingRoomId,
        Long appointmentId) {
}

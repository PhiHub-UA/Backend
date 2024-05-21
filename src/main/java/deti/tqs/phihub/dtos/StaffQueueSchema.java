package deti.tqs.phihub.dtos;

public record StaffQueueSchema(Long id, Integer maxSize, String showingLetter, int ticketCounter, int queueSize) {
    
}

package service;

import model.parking.ParkingSpot;
import model.ticket.Ticket;
import model.vehicle.Vehicle;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TicketService {

    private final Map<String, Ticket> ticketMap = new ConcurrentHashMap<>();

    public Ticket createTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), vehicle, parkingSpot);
        ticketMap.put(ticket.getId(), ticket);
        return ticket;
    }

    public void consumeTicket(Ticket ticket) {
        if (!ticketMap.containsKey(ticket.getId())) {
            throw new IllegalStateException("This ticket is invalid");
        }

        ticketMap.remove(ticket.getId());
    }
}

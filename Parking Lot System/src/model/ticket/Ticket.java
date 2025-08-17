package model.ticket;

import model.parking.ParkingSpot;
import model.vehicle.Vehicle;

import java.time.Instant;

public class Ticket {

    private final String id;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final Instant entryInstant;

    public Ticket(String id, Vehicle vehicle, ParkingSpot spot) {
        this.id = id;
        this.vehicle = vehicle;
        this.spot = spot;
        entryInstant = Instant.now();
    }

    public String getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public Instant getEntryInstant() {
        return entryInstant;
    }
}

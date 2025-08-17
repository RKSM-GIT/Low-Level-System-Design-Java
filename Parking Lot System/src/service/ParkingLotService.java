package service;

import model.parking.ParkingFloor;
import model.parking.ParkingSpot;
import model.ticket.Receipt;
import model.ticket.Ticket;
import model.vehicle.Vehicle;
import strategy.allocation.AllocationStrategy;
import strategy.cost.CostStrategy;

import java.time.Instant;
import java.util.List;

public class ParkingLotService {

    private List<ParkingFloor> floors;
    private final TicketService ticketService;
    private CostStrategy costCalculator;
    private AllocationStrategy allocator;

    public ParkingLotService(List<ParkingFloor> floors) {
        this.ticketService = TicketService.getInstance();
        this.floors = floors;
    }

    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }

    public void removeFloor(ParkingFloor floor) {
        floors.remove(floor);
    }

    public void setCostStrategy(CostStrategy costStrategy) {
        this.costCalculator = costStrategy;
    }

    public void setAllocationStrategy(AllocationStrategy allocationStrategy) {
        this.allocator = allocationStrategy;
    }

    public synchronized Ticket parkVehicle(Vehicle vehicle) {
        ParkingSpot allocatedSpot = null;
        for (ParkingFloor floor : floors) {
            allocatedSpot = allocator.allocate(floors, vehicle);
            if (allocatedSpot != null) {
                break;
            }
        }

        if (allocatedSpot == null) {
            return null;
        }

        return ticketService.createTicket(vehicle, allocatedSpot);
    }

    public synchronized Receipt unparkVehicle(Ticket ticket) {
        ParkingSpot spot = ticket.getSpot();
        for (ParkingFloor floor : floors) {
            if (spot.getFloorNo() == floor.getFloorNo()) {
                floor.unassignSpot(ticket.getVehicle());
                break;
            }
        }


        Instant exitInstant = Instant.now();
        long cost = costCalculator.calculateCost(ticket, exitInstant);
        return new Receipt(ticket, cost, exitInstant);
    }
}

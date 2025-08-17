package strategy.allocation;

import model.parking.ParkingFloor;
import model.parking.ParkingSpot;
import model.vehicle.Vehicle;

import java.util.List;

public interface AllocationStrategy {

    ParkingSpot allocate(List<ParkingFloor> floors, Vehicle vehicle);
}

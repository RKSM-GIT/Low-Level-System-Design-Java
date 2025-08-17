package strategy.allocation;

import enums.SpotType;
import enums.VehicleType;
import model.parking.ParkingFloor;
import model.parking.ParkingSpot;
import model.vehicle.Vehicle;

import java.util.List;
import java.util.Map;

public class NearestSizeStrategy implements AllocationStrategy {

    private final Map<VehicleType, List<SpotType>> prefMap = Map.of(
            VehicleType.MOTORCYCLE, List.of(SpotType.SMALL, SpotType.MEDIUM, SpotType.LARGE),
            VehicleType.CAR, List.of(SpotType.MEDIUM, SpotType.LARGE),
            VehicleType.TRUCK, List.of(SpotType.LARGE)
    );

    @Override
    public ParkingSpot allocate(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.tryAssignSpot(vehicle, prefMap.get(vehicle.getVehicleType()));

            if (spot != null) {
                return spot;
            }
        }

        return null;
    }
}

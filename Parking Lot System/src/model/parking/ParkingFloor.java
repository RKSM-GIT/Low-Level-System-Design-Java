package model.parking;

import enums.SpotType;
import model.vehicle.Vehicle;

import java.util.*;

public class ParkingFloor {

    private final int floorNo;
    private final Map<SpotType, PriorityQueue<ParkingSpot>> freeSpotsByType = new EnumMap<>(SpotType.class);
    private final Map<String, ParkingSpot> occupiedSpots = new HashMap<>();

    public ParkingFloor(int floorNo, List<ParkingSpot> parkingSpots) {
        this.floorNo = floorNo;

        Arrays.stream(SpotType.values()).forEach(type -> freeSpotsByType.put(type, new PriorityQueue<>()));

        parkingSpots.forEach(spot -> {
            if (spot.isFree()) {
                freeSpotsByType.get(spot.getSpotType()).add(spot);
            } else {
                occupiedSpots.put(spot.getOccupiedBy().getLicense(), spot);
            }
        });
    }

    public synchronized ParkingSpot tryAssignSpot(Vehicle vehicle, List<SpotType> preference) {
        if (preference == null || preference.isEmpty()) {
            return null;
        }

        for (SpotType type : preference) {
            var pq = freeSpotsByType.get(type);
            if (pq.isEmpty()) {
                continue;
            }

            ParkingSpot spot = pq.poll();
            spot.allocateVehicle(vehicle);
            occupiedSpots.put(vehicle.getLicense(), spot);
            return spot;
        }

        return null;
    }

    public synchronized void unassignSpot(Vehicle vehicle) {
        if (!occupiedSpots.containsKey(vehicle.getLicense())) {
            throw new IllegalStateException("This vehicle was never assigned to this floor");
        }

        ParkingSpot spot = occupiedSpots.remove(vehicle.getLicense());
        spot.deallocateVehicle();
        freeSpotsByType.get(spot.getSpotType()).add(spot);
    }

    public int getFloorNo() {
        return floorNo;
    }
}

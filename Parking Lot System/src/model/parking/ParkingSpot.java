package model.parking;

import enums.SpotType;
import model.vehicle.Vehicle;

public class ParkingSpot implements Comparable<ParkingSpot> {

    private final String id;
    private final Integer floorNo;
    private Vehicle occupiedBy;
    private final SpotType spotType;
    private final Double distanceFromEntrance;

    public ParkingSpot(String id, Integer floorNo, SpotType spotType, Double distanceFromEntrance) {
        this.id = id;
        this.floorNo = floorNo;
        this.spotType = spotType;
        this.distanceFromEntrance = distanceFromEntrance;
    }

    public boolean isFree() {
        return occupiedBy == null;
    }

    public void allocateVehicle(Vehicle vehicle) {
        if (!isFree()) {
            throw new IllegalStateException("Parking spot is already allocated");
        }
        occupiedBy = vehicle;
    }

    public void deallocateVehicle() {
        occupiedBy = null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Integer getFloorNo() {
        return floorNo;
    }

    public Vehicle getOccupiedBy() {
        return occupiedBy;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public Double getDistanceFromEntrance() {
        return distanceFromEntrance;
    }

    // Comparator
    @Override
    public int compareTo(ParkingSpot o) {
        int distComp = Double.compare(distanceFromEntrance, o.distanceFromEntrance);
        return distComp == 0 ? id.compareTo(o.getId()) : distComp;
    }
}

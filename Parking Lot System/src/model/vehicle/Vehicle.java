package model.vehicle;

import enums.VehicleType;

public class Vehicle {

    private final String license;
    private final VehicleType vehicleType;

    public Vehicle(String license, VehicleType vehicleType) {
        this.license = license;
        this.vehicleType = vehicleType;
    }

    public String getLicense() {
        return license;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}

package model.vehicle;

import enums.VehicleType;

public class Truck extends Vehicle {

    public Truck(String license) {
        super(license, VehicleType.TRUCK);
    }
}

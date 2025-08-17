package model.vehicle;

import enums.VehicleType;

public class Motorcycle extends Vehicle {

    public Motorcycle(String license) {
        super(license, VehicleType.MOTORCYCLE);
    }
}

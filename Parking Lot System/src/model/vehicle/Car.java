package model.vehicle;

import enums.VehicleType;

public class Car extends Vehicle {

    public Car(String license) {
        super(license, VehicleType.CAR);
    }
}

import enums.SpotType;
import model.parking.ParkingFloor;
import model.parking.ParkingSpot;
import model.vehicle.Car;
import model.vehicle.Motorcycle;
import model.vehicle.Truck;
import model.vehicle.Vehicle;
import service.ParkingLotService;
import strategy.allocation.NearestSizeStrategy;
import strategy.cost.HourlyCostStrategy;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Step 1: Build parking spots
        List<ParkingSpot> floor1Spots = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            floor1Spots.add(new ParkingSpot("C" + i, 1, SpotType.MEDIUM, (double) i));
        }
        floor1Spots.add(new ParkingSpot("M1", 1, SpotType.SMALL, 4.0));
        floor1Spots.add(new ParkingSpot("T1", 1, SpotType.LARGE, 5.0));

        // Step 2: Create a floor
        ParkingFloor floor1 = new ParkingFloor(1, floor1Spots);

        // Step 3: Create parking lot and ticket service
        ParkingLotService lot = new ParkingLotService(new ArrayList<>(List.of(floor1)));
        lot.setAllocationStrategy(new NearestSizeStrategy());
        lot.setCostStrategy(new HourlyCostStrategy());

        // Step 4: Vehicles arrive
        Vehicle car1 = new Car("KA-01-1234");
        Vehicle car2 = new Car("KA-01-9999");
        Vehicle bike = new Motorcycle("KA-02-5555");

        // Step 5: Park vehicles and issue tickets
        System.out.println("=== Vehicles Entering ===");
        var ticket1 = lot.parkVehicle(car1);
        System.out.println("Ticket issued for " + car1.getLicense() + " -> " + ticket1.getId());

        var ticket2 = lot.parkVehicle(car2);
        System.out.println("Ticket issued for " + car2.getLicense() + " -> " + ticket2.getId());

        var ticket3 = lot.parkVehicle(bike);
        System.out.println("Ticket issued for " + bike.getLicense() + " -> " + ticket3.getId());

        // Step 6: One vehicle leaves
        System.out.println("\n=== Vehicle Exiting ===");
        lot.unparkVehicle(ticket1);
        System.out.println("Car " + car1.getLicense() + " has exited.");

        // Step 7: Another vehicle arrives
        Vehicle truck = new Truck("KA-03-8888");
        var ticket4 = lot.parkVehicle(truck);
        System.out.println("Ticket issued for " + truck.getLicense() + " -> " + ticket4.getId());

        // Final state
        System.out.println("\n=== Simulation Complete ===");
    }
}
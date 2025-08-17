package strategy.cost;

import enums.VehicleType;
import model.ticket.Ticket;

import java.time.Instant;
import java.util.Map;

public class HourlyCostStrategy implements CostStrategy {

    private final Map<VehicleType, Long> rates = Map.of(
            VehicleType.MOTORCYCLE, 10L,
            VehicleType.CAR, 20L,
            VehicleType.TRUCK, 30L
    );

    @Override
    public long calculateCost(Ticket ticket, Instant exitTime) {
        Instant entryTime = ticket.getEntryInstant();
        long hoursSpent = entryTime.until(exitTime).toHours();

        return hoursSpent * rates.get(ticket.getVehicle().getVehicleType());
    }
}

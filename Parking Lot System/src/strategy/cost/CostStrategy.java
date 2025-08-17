package strategy.cost;

import model.ticket.Ticket;

import java.time.Instant;

public interface CostStrategy {

    long calculateCost(Ticket ticket, Instant exitTime);
}

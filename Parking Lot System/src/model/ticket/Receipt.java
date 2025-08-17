package model.ticket;

import java.time.Instant;

public class Receipt {

    private final Ticket ticket;
    private final int amount;
    private final Instant exitInstant;

    public Receipt(Ticket ticket, int amount, Instant exitInstant) {
        this.ticket = ticket;
        this.amount = amount;
        this.exitInstant = exitInstant;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public int getAmount() {
        return amount;
    }

    public Instant getExitInstant() {
        return exitInstant;
    }
}

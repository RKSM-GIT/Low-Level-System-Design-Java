package model.ticket;

import java.time.Instant;

public class Receipt {

    private final Ticket ticket;
    private final long amount;
    private final Instant exitInstant;

    public Receipt(Ticket ticket, long amount, Instant exitInstant) {
        this.ticket = ticket;
        this.amount = amount;
        this.exitInstant = exitInstant;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public long getAmount() {
        return amount;
    }

    public Instant getExitInstant() {
        return exitInstant;
    }
}

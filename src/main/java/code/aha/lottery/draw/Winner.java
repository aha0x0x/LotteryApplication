package code.aha.lottery.draw;

import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author aha
 */
public class Winner 
{
    private static enum Position
    {
        FIRST, SECOND, THIRD
    }
    
    private final int number;
    private final Position position;
    private final Optional<Ticket> ticket;
    private final int amount;
    
    private Winner(Position pos, int number, int amount, Ticket ticket)
    {
        this.number = number;
        this.position = pos;
        this.ticket = Optional.ofNullable(ticket);
        this.amount = amount;
    }

    public static Winner createFistPosition( int number, Ticket ticket, int amount){
        return new Winner(Position.FIRST, number, amount, ticket);
    }

    public static Winner createSecondPosition( int number, Ticket ticket, int amount){
        return new Winner(Position.SECOND, number, amount, ticket);
    }

    public static Winner createThirdPosition( int number, Ticket ticket, int amount){
        return new Winner(Position.THIRD, number, amount, ticket);
    }

    public int getAmount() {
        return amount;
    }
    
    public Optional<Ticket> getTicket(){
        return ticket;
    }
    
    public boolean isFirst(){
        return this.position == Position.FIRST;
    }
    
    public boolean isSecond(){
        return this.position == Position.SECOND;
    }
    
    public boolean isThird(){
        return this.position == Position.THIRD;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.number;
        hash = 71 * hash + Objects.hashCode(this.position);
        hash = 71 * hash + Objects.hashCode(this.ticket);
        hash = 71 * hash + this.amount;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Winner other = (Winner) obj;
        if (this.number != other.number) {
            return false;
        }
        if (this.amount != other.amount) {
            return false;
        }
        if (this.position != other.position) {
            return false;
        }
        if (!Objects.equals(this.ticket, other.ticket)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Winner[" + "number=" + number + ", position=" + position + ", ticket=" + ticket + ", amount=" + amount + ']';
    }
    
}

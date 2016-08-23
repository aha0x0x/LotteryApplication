package code.aha.lottery.draw;

import java.util.Objects;

/**
 *
 * @author aha
 */
public class Ticket 
{
    private final int number;
    private final String name;
    
    public Ticket( int number, String name )
    {
        this.name = name;
        this.number = number;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.number;
        hash = 97 * hash + Objects.hashCode(this.name);
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
        final Ticket other = (Ticket) obj;
        if (this.number != other.number) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Ticket{" + "number=" + number + ", name=" + name + '}';
    }
    
    
    

}

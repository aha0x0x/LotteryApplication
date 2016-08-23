package code.aha.lottery.draw;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author aha
 */
public class DefaultDrawManager implements DrawManager
{
    private final int range;
    private double money;
    private Map<Integer,Ticket> purchased = new HashMap<>();
    private final NumberGenerator numberGenerator;
    
    private Optional<Map<Integer,Winner>> winners;
    
    
    @Inject
    public DefaultDrawManager(NumberGenerator numberGenerator, 
                              @Named("INITIAL_POOL") double money, 
                              @Named("DRAW_RANGE") int range)
    {
        this.numberGenerator = numberGenerator;
        this.range = range;
        this.money = money;
        this.winners = Optional.empty();
    }
    
    @Override
    public synchronized void startNewDraw()
    {
        purchased.clear();
        winners = Optional.empty();
    }
    
    @Override
    public synchronized Ticket purchaseTicket(String user) throws DrawManagerException
    {
        if(purchased.keySet().size() == range){
            throw new DrawManagerException("all tickets sold");
        }

        int number = generateNewTicketNumber();
        Ticket ticket = new Ticket(number, user);
        
        money += 10;
        purchased.put(number, ticket);
        
        return ticket;
    }
    
    private int generateNewTicketNumber() 
    {
        //TODO: this may take foreever. fix
        int generated = numberGenerator.next();
        while(purchased.containsKey(generated)){
            generated = numberGenerator.next();
        }
        return generated;
    }
    
    @Override
    public synchronized Map<Integer,Winner> getWinners()
    {
        if( !winners.isPresent() )
        {
            /**
             *  we need to do the following:
             *  1, do the draw
             *  2. check if a corresponding ticket is found, if so subtract the winnings from the money pool 
             *  3. subtract the payout from the pool of money
             * 
             */
            int firstDraw = numberGenerator.next();
            int secondDraw = numberGenerator.next();
            int thirdDraw = numberGenerator.next();
        
            Map<Integer,Winner> winnerMap = new HashMap<>();
        
            double payout = 0;
        
            Double firstPrize = 0.75 * ( money / 2.0 );
            if(purchased.containsKey(firstDraw)){
                payout += firstPrize;
            }
            winnerMap.put(1, Winner.createFistPosition(firstDraw,purchased.get(firstDraw),firstPrize.intValue()));
        
            Double secondPrize = 0.15 * ( money / 2.0 );
            if(purchased.containsKey(secondDraw)){
                payout += secondPrize;
            }
            winnerMap.put(2, Winner.createSecondPosition(secondDraw,purchased.get(secondDraw),secondPrize.intValue()));
        
            Double thirdPrize = 0.10 * ( money / 2.0 );
            if(purchased.containsKey(thirdDraw)){
                payout += thirdPrize;
            }
            winnerMap.put(3, Winner.createThirdPosition(thirdDraw,purchased.get(thirdDraw),thirdPrize.intValue()));
                
            winners = Optional.of(winnerMap);
            money -= payout;
        }
        
        return new HashMap<>(winners.get());
    }
    
    @Override
    public synchronized double getCurrentPool()
    {
    	return money;
    }
    
    @Override 
    public synchronized Set<Ticket> getTicketsSold()
    {
    	return new HashSet<Ticket>(purchased.values());
    }
    
    
}

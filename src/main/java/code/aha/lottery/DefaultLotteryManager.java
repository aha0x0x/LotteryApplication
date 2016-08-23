package code.aha.lottery;

import code.aha.lottery.draw.DrawManager;
import code.aha.lottery.draw.DrawManagerException;
import code.aha.lottery.input.Command;
import code.aha.lottery.draw.Ticket;
import code.aha.lottery.draw.Winner;
import code.aha.lottery.input.InputHandler;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Default implementation of LotteryManager
 * @author aha
 */
class DefaultLotteryManager implements LotteryManager 
{
    private final InputHandler inputHandler;
    private final DrawManager drawManager;
    private final PrintStream printer;
    
    @Inject
    public DefaultLotteryManager(InputHandler inputHandler, 
    						     DrawManager drawManager,
    						     @Named("OUTPUT_STREAM") PrintStream printer )
    {
        this.inputHandler = inputHandler;
        this.drawManager = drawManager;
        this.printer = printer;
        
    }
    
    public void start() throws LotteryManagerException
    {
        while(true)
        {
            Optional<Command> cmd = getInput(); 
            if( !cmd.isPresent() ){
                continue;
            }
            
            if( cmd.get().isPurchaseCommand())
            {
                if(!cmd.get().getArguments().isPresent()){
                    continue;
                }else
                {
                    Ticket ticket = purchaseTicket(cmd);
                    printer.println(ticket.getNumber());
                }
            }
            else if( cmd.get().isDrawCommand()){
                drawManager.startNewDraw();
            }
            else if( cmd.get().isWinnersCommand()){
                Map<Integer, Winner> winners = drawManager.getWinners();
                printResult( winners );
            }
            else if( cmd.get().isQuitCommand()){
                break;
            }
            else{
                throw new LotteryManagerException("Unknown Command" + cmd.get() + ". Not supported yet");
            }
        }
    }

    private Ticket purchaseTicket(Optional<Command> cmd) throws LotteryManagerException 
    {
    	List<String> args = cmd.get().getArguments().get();
        String user = args.get(0);

        try {
            return drawManager.purchaseTicket(user);
        } catch (DrawManagerException e) {
            throw new LotteryManagerException("failed to purchase ticket", e);
        }
    }

    private void printResult(Map<Integer, Winner> winners) 
    {
        Winner first = winners.get(1);
        Winner second = winners.get(2);
        Winner third = winners.get(3);
        
        StringBuilder builder = new StringBuilder();
        if( first.getTicket().isPresent())
        {
            Ticket ticket = first.getTicket().get();
            builder.append(ticket.getName()).append(":").append(Integer.toString(first.getAmount())).append("$");
        }
        else{
            builder.append("NONE").append(":").append(Integer.toString(first.getAmount())).append("$");
        }
        builder.append("\t");
        
        if( second.getTicket().isPresent())
        {
            Ticket ticket = second.getTicket().get();
            builder.append(ticket.getName()).append(":").append(Integer.toString(second.getAmount())).append("$");
        }
        else{
            builder.append("NONE").append(":").append(Integer.toString(second.getAmount())).append("$");
        }
        builder.append("\t");
        
        if( third.getTicket().isPresent())
        {
            Ticket ticket = third.getTicket().get();
            builder.append(ticket.getName()).append(":").append(Integer.toString(third.getAmount())).append("$");
        }
        else{
            builder.append("NONE").append(":").append(Integer.toString(third.getAmount())).append("$");
        }
        
        
        printer.println("1st ball\t2nd ball\t3rd ball");
        printer.println(builder.toString());
    }

    private Optional<Command> getInput() throws LotteryManagerException 
    {
        try {
            return inputHandler.next();
        } catch (IOException e) {
            throw new LotteryManagerException("failed to get input", e);
        }
    }
    
    


}

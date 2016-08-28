package code.aha.lottery;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;

import code.aha.lottery.draw.DrawManager;
import code.aha.lottery.draw.Ticket;
import code.aha.lottery.draw.Winner;
import code.aha.lottery.input.Command;
import code.aha.lottery.input.InputHandler;

/**
 *
 * @author aha
 */
public class DefaultLotteryManagerTest {
    
    private DefaultLotteryManager lotteryManager;
    private DrawManager drawManager;
    private InputHandler inputHandler;
    private ByteArrayOutputStream byteOutputStream;
    private PrintStream printer;
    
    @Before
    public void setUp() 
    {
        this.inputHandler = mock(InputHandler.class);
        this.drawManager = mock(DrawManager.class);
        this.byteOutputStream = new ByteArrayOutputStream();
        this.printer = new PrintStream(this.byteOutputStream);
        
        Injector injector = Guice.createInjector( Modules.override(new MainModule() ).with(
                new AbstractModule() {
                        @Override
                        protected void configure() 
                        {
                            bind(InputHandler.class).toInstance(inputHandler);
                            bind(DrawManager.class).toInstance(drawManager);
                            bind(PrintStream.class).annotatedWith(Names.named("OUTPUT_STREAM")).toInstance(printer);
                        }
                }) );
        
        lotteryManager = injector.getInstance(DefaultLotteryManager.class);
    }
    
    @After
    public void tearDown() 
    {
        lotteryManager = null;
        printer.close();
        try {
			byteOutputStream.close();
		} catch (IOException e) {
			//nothing to do
		}
    }

    /**
     * Test of start method, of class DefaultLotteryManager.
     */
    @Test
    public void testPurchaseCommand() throws Exception 
    {
        String user = "HANNAN";
        String cmd = "purchase " + user;
        Ticket expectedTicket = new Ticket(5, user);
        
        when(inputHandler.next()).thenReturn(Command.fromInput(cmd)).thenReturn(Command.fromInput("quit"));
        when(drawManager.purchaseTicket(user)).thenReturn(expectedTicket);
        
        lotteryManager.start();
        
        String expectedOutput = Integer.toString(expectedTicket.getNumber()) + "\n"; 
        String actualOutput = new String(this.byteOutputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
        
        verify(inputHandler,times(2)).next();
        verifyNoMoreInteractions(inputHandler);
        verify(drawManager, times(1)).purchaseTicket(user);
        verifyNoMoreInteractions(drawManager);
    }
    
    @Test
    public void testPurchaseCommandWithNoUserGiven() throws Exception 
    {
        String user = "";
        String cmd = "purchase " + user;
        
        when(inputHandler.next()).thenReturn(Command.fromInput(cmd)).thenReturn(Command.fromInput("quit"));
        lotteryManager.start();
        
        verify(inputHandler,times(2)).next();
        verifyNoMoreInteractions(inputHandler);
        verifyNoMoreInteractions(drawManager);
    }
    
    @Test
    public void testDrawCommand() throws Exception 
    {
        String cmd = "draw";
        when(inputHandler.next()).thenReturn(Command.fromInput(cmd)).thenReturn(Command.fromInput("quit"));
        lotteryManager.start();
        
        verify(inputHandler,times(2)).next();
        verifyNoMoreInteractions(inputHandler);
        verify(drawManager, times(1)).startNewDraw();
        verifyNoMoreInteractions(drawManager);
    }
    
    @Test
    public void testWinnersCommand() throws Exception 
    {
        String cmd = "winners";
        Map<Integer,Winner> winners = createWinnerList();
        when(inputHandler.next()).thenReturn(Command.fromInput(cmd)).thenReturn(Command.fromInput("quit"));
        when(drawManager.getWinners()).thenReturn(winners);
        
        lotteryManager.start();
        
        verify(inputHandler,times(2)).next();
        verifyNoMoreInteractions(inputHandler);
        verify(drawManager, times(1)).getWinners();
        verifyNoMoreInteractions(drawManager);
        
        String expectedOutput = createWinnerOutput(winners);
        String actualOutput = new String(this.byteOutputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testInvalidInput() throws Exception
    {
    	String cmd = "yahoo";
        when(inputHandler.next()).thenReturn(Command.fromInput(cmd)).thenReturn(Command.fromInput("quit"));
        lotteryManager.start();
        
        verify(inputHandler,times(2)).next();
        verifyNoMoreInteractions(inputHandler);
        verifyNoMoreInteractions(drawManager);
    }
    
    @Test
    public void testEmptyInput() throws Exception
    {
    	String cmd = "";
        when(inputHandler.next()).thenReturn(Command.fromInput(cmd)).thenReturn(Command.fromInput("quit"));
        lotteryManager.start();
        
        verify(inputHandler,times(2)).next();
        verifyNoMoreInteractions(inputHandler);
        verifyNoMoreInteractions(drawManager);
    }
    
    @Test
    public void testFullRound() throws Exception
    {
    	String user1 = "HANNAN";
    	String user2 = "ANNA";
    	String user3 = "JOE";
    	
    	String cmd1 = "draw";
    	String cmd2 = "purchase " + user1;
    	String cmd3 = "purchase " + user2;
    	String cmd4 = "purchase " + user3;
    	String cmd5 = "winners ";
    	String cmd6 = "quit";

    	Ticket expectedTicket1 = new Ticket(5, user1);
    	Ticket expectedTicket2 = new Ticket(13, user2);
    	Ticket expectedTicket3 = new Ticket(21, user3);
    	
    	Map<Integer,Winner> winners = new HashMap<>();
    	winners.put(1, Winner.createFistPosition(expectedTicket1.getNumber(),expectedTicket1,75));
    	winners.put(2, Winner.createSecondPosition(expectedTicket2.getNumber(),expectedTicket2,15));
    	winners.put(3, Winner.createThirdPosition(expectedTicket3.getNumber(),expectedTicket3,10));
    	
    	when(drawManager.purchaseTicket(user1)).thenReturn(expectedTicket1);
    	when(drawManager.purchaseTicket(user2)).thenReturn(expectedTicket2);
    	when(drawManager.purchaseTicket(user3)).thenReturn(expectedTicket3);
    	when(drawManager.getWinners()).thenReturn(winners);    	
        
        when(inputHandler.next()).thenReturn(Command.fromInput(cmd1))
        						 .thenReturn(Command.fromInput(cmd2))
        						 .thenReturn(Command.fromInput(cmd3))
        						 .thenReturn(Command.fromInput(cmd4))
        						 .thenReturn(Command.fromInput(cmd5))
        						 .thenReturn(Command.fromInput(cmd6));
        						 
        lotteryManager.start();
        
        verify(inputHandler,times(6)).next();
        verifyNoMoreInteractions(inputHandler);
        
        StringBuilder builder = new StringBuilder();
        builder.append(Integer.toString(expectedTicket1.getNumber())).append("\n");
        builder.append(Integer.toString(expectedTicket2.getNumber())).append("\n");
        builder.append(Integer.toString(expectedTicket3.getNumber())).append("\n");
        builder.append(createWinnerOutput(winners));
        
        String expectedOutput = builder.toString(); 
        String actualOutput = new String(this.byteOutputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
        
        verify(drawManager,times(1)).startNewDraw();
        verify(drawManager,times(1)).purchaseTicket(user1);
        verify(drawManager,times(1)).purchaseTicket(user2);
        verify(drawManager,times(1)).purchaseTicket(user3);
        verify(drawManager,times(1)).getWinners();
        verifyNoMoreInteractions(drawManager);
    }
    
    
    private String createWinnerOutput(Map<Integer, Winner> winners) 
    {
    	Winner first = winners.get(1);
        Winner second = winners.get(2);
        Winner third = winners.get(3);
        
        StringBuilder builder = new StringBuilder();
        builder.append("1st ball\t2nd ball\t3rd ball\n");
        
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
        builder.append("\n");
        return builder.toString();
	}
    

	private Map<Integer, Winner> createWinnerList()
    {
    	Map<Integer,Winner> winMap = new HashMap<>();
    	winMap.put(1, Winner.createFistPosition(1, new Ticket(20, "HANNAN"), 75));
    	winMap.put(2, Winner.createFistPosition(1, new Ticket(43, "ANNA"), 15));
    	winMap.put(3, Winner.createFistPosition(1, new Ticket(20, "JOE"), 10));
    	return winMap;
    	
    }
    
    
}

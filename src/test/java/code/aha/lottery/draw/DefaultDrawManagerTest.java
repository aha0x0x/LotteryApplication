package code.aha.lottery.draw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;

import code.aha.lottery.MainModule;

/**
 *
 * @author aha
 */
public class DefaultDrawManagerTest 
{
    private NumberGenerator numberGen;
    private DrawManager drawManager;
    
    @Before
    public void setUp() 
    {
        this.numberGen = mock(NumberGenerator.class);
        
        
        Injector injector = Guice.createInjector( Modules.override(new MainModule() ).with(
                new AbstractModule() {
                        @Override
                        protected void configure() 
                        {
                            bind(NumberGenerator.class).toInstance(numberGen);
                        }
                }) );
        
        this.drawManager = injector.getInstance(DrawManager.class);
    }
    
    @After
    public void tearDown() 
    {
    }

    @Test
    public void testStartNewDraw() throws DrawManagerException 
    {
    	// test new draw 
    	// does not reset the pool money
    	// 
    	String user = "HANNAN";
    	
    	drawManager.purchaseTicket(user);
    	double before = drawManager.getCurrentPool();
    	
    	drawManager.startNewDraw();
    	double after = drawManager.getCurrentPool();
    	
    	assertTrue(before == after);
       
    }
    
    
    @Test
    public void testPurchaseTicket() throws DrawManagerException
    {
    	// After a purchase 
    	// check pool amount increment 
    	// check addition in purchased list
    	
    	String user = "HANNAN";
    	when(numberGen.next()).thenReturn(10).thenReturn(20);
    	
    	double amountBefore = drawManager.getCurrentPool();
    	
    	Collection<Ticket> beforePurchase = drawManager.getTicketsSold();
    	
    	Ticket ticket = drawManager.purchaseTicket(user);
    	
    	double expected = amountBefore + 10;
    	double actual = drawManager.getCurrentPool();
    	
    	assertTrue(expected == actual);
    	
    	Collection<Ticket> afterPurchase = drawManager.getTicketsSold();
    	
    	assertFalse( beforePurchase.contains(ticket));
    	assertTrue( afterPurchase.contains(ticket));
    }
    
    @Test
    public void testStartNewDrawPoolResetPurchases() throws DrawManagerException 
    {
    	String user = "HANNAN";
    	when(numberGen.next()).thenReturn(10).thenReturn(20);
    	
    	Ticket ticket = drawManager.purchaseTicket(user);
    	
    	Collection<Ticket> currentPurchases = drawManager.getTicketsSold();
    	assertTrue(currentPurchases.contains(ticket));
    	
    	drawManager.startNewDraw();
    	Collection<Ticket> newPurchases = drawManager.getTicketsSold();
    	assertTrue(newPurchases.isEmpty());
    }
    
    @Test
    public void testPrizeMoneyAndRemainingPoolWhenAllWinningTicketsSold() throws DrawManagerException
    {
    	// test 
    	// prize money for each winner   
    	// remaining amount in the pool
    	// 
    	String user1 = "HANNAN";
    	String user2 = "ANNA";
    	String user3 = "JOE";
    	
    	// first three numbers for tickets
    	// the last three for calculating winners
    	when(numberGen.next()).thenReturn(10).thenReturn(20).thenReturn(30).thenReturn(10).thenReturn(20).thenReturn(30);
    	
    	Ticket t1 = drawManager.purchaseTicket(user1);
    	Ticket t2 = drawManager.purchaseTicket(user2);
    	Ticket t3 = drawManager.purchaseTicket(user3);
    	
    	double currentPool = drawManager.getCurrentPool();
    	
    	double firstPrize = (currentPool / 2 ) * 0.75;
    	double secondPrize = (currentPool / 2 ) * 0.15;
    	double thirdPrize = (currentPool / 2 ) * 0.10;
    	
    	Winner expectedFirst  = Winner.createFistPosition(t1.getNumber(), t1, new Double(firstPrize).intValue() );
    	Winner expectedSecond = Winner.createSecondPosition(t2.getNumber(), t2, new Double(secondPrize).intValue() );
    	Winner expectedThird  = Winner.createThirdPosition(t3.getNumber(), t3, new Double(thirdPrize).intValue() );
    	
    	Map<Integer,Winner> winners = drawManager.getWinners();
    	
    	assertEquals(expectedFirst, winners.get(1));
    	assertEquals(expectedSecond, winners.get(2));
    	assertEquals(expectedThird, winners.get(3));
    	
    	double expectedRemaining = currentPool - firstPrize - secondPrize - thirdPrize;
    	assertTrue(expectedRemaining == drawManager.getCurrentPool());
    	
    }
    
    @Test
    public void testPrizeMoneyAndRemainingPoolWhenSomeWinningTicketSold() throws DrawManagerException
    {
    	String user1 = "HANNAN";
    	String user2 = "ANNA";
    	String user3 = "JOE";
    	
    	// first three numbers for tickets
    	// the last three for calculating winners -- no prize for third user #33 
    	when(numberGen.next()).thenReturn(10).thenReturn(20).thenReturn(30).thenReturn(10).thenReturn(20).thenReturn(33);
    	
    	Ticket t1 = drawManager.purchaseTicket(user1);
    	Ticket t2 = drawManager.purchaseTicket(user2);
    	Ticket t3 = drawManager.purchaseTicket(user3);
    	
    	double currentPool = drawManager.getCurrentPool();
    	
    	double firstPrize = (currentPool / 2 ) * 0.75;
    	double secondPrize = (currentPool / 2 ) * 0.15;
    	double thirdPrize = (currentPool / 2 ) * 0.10;
    	
    	Winner expectedFirst  = Winner.createFistPosition(t1.getNumber(), t1, new Double(firstPrize).intValue() );
    	Winner expectedSecond = Winner.createSecondPosition(t2.getNumber(), t2, new Double(secondPrize).intValue() );
    	
    	Winner expectedThird  = Winner.createThirdPosition(33, null, new Double(thirdPrize).intValue() );
    	
    	Map<Integer,Winner> winners = drawManager.getWinners();
    	
    	assertEquals(expectedFirst, winners.get(1));
    	assertEquals(expectedSecond, winners.get(2));
    	assertEquals(expectedThird, winners.get(3));
    	
    	double expectedRemaining = currentPool - firstPrize - secondPrize;
    	assertTrue(expectedRemaining == drawManager.getCurrentPool());
    	
    }
}

package code.aha.lottery.draw;

import java.util.Map;
import java.util.Set;

public interface DrawManager 
{
	void startNewDraw();
	Ticket purchaseTicket(String user) throws DrawManagerException;
	Map<Integer,Winner> getWinners();
	
	double getCurrentPool();
	Set<Ticket> getTicketsSold();
}

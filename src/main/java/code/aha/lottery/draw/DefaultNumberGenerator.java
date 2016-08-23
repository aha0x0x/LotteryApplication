package code.aha.lottery.draw;

import java.util.Random;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DefaultNumberGenerator implements NumberGenerator 
{
	private final int range;
	private final Random random;
	
	@Inject
	public DefaultNumberGenerator(@Named("DRAW_RANGE") int range) 
	{
		this.range = range;
		this.random = new Random(System.currentTimeMillis());
	}
	
	
	@Override
	public int next() 
	{
		return random.nextInt(range) + 1;
	}
}

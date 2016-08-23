package code.aha.lottery;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 *
 * @author aha
 */
public class Lottery 
{
    public static void main(String[] args)
    {
    	Injector injector = Guice.createInjector(new MainModule()); 
        LotteryManager manager = injector.getInstance(LotteryManager.class);
        try 
        {
            manager.start();
        } 
        catch (LotteryManagerException ex) 
        {
            System.out.println("Abnormal Exit.Exception thrown running LotteryManager" + ex.toString());
        } 
    }


}

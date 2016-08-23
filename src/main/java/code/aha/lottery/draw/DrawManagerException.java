package code.aha.lottery.draw;

/**
 * Exception class to wrap exceptions thrown by DrawManager
 * @author aha
 */
public class DrawManagerException extends Exception 
{
 	private static final long serialVersionUID = 2892551357199772742L;

	public DrawManagerException(String msg){
        super(msg);
    }
    
    public DrawManagerException(String msg, Throwable t){
        super(msg,t);
    }
    
}

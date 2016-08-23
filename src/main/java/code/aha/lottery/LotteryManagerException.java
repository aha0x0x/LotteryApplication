package code.aha.lottery;

/**
 * Exception class to wrap all standard java exceptions
 * @author aha
 */
class LotteryManagerException extends Exception 
{
    private static final long serialVersionUID = -3330912236745575739L;

	public LotteryManagerException(String msg){
        super(msg);
    }
    
    public LotteryManagerException(Throwable t){
        super(t);
    }
        
    public LotteryManagerException(String msg, Throwable t){
        super(msg,t);
    }
}

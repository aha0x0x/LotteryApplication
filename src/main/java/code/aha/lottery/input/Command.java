package code.aha.lottery.input;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This type represents user command given to the system.
 * It will also provide any command line arguments given with the command
 * @author aha
 */
public class  Command 
{
    // an internal enum to represent various commands. 
    private static enum Type
    {
        PURCHASE("PURCHASE"), 
        DRAW("DRAW"), 
        WINNERS("WINNERS"), 
        QUIT("QUIT");

        private String text;
    
        Type(String text){
        this.text = text;
        }
    
        public String getText(){
        return this.text;
        }
    }
    
    private final Type type;
    private final String raw;
    private final Optional<List<String>> args;
    
    private Command(Type type, List<String> args, String raw)
    {
        this.type = type;
        this.args = Optional.ofNullable(args);
        this.raw = raw;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((args == null) ? 0 : args.hashCode());
		result = prime * result + ((raw == null) ? 0 : raw.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (args == null) {
			if (other.args != null)
				return false;
		} else if (!args.equals(other.args))
			return false;
		if (raw == null) {
			if (other.raw != null)
				return false;
		} else if (!raw.equals(other.raw))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Command [type=" + type + ", raw=" + raw + ", args=" + args + "]";
	}

	public boolean isPurchaseCommand(){
        return this.type == Type.PURCHASE;
    }
    
    public boolean isDrawCommand(){
        return this.type == Type.DRAW;
    }
    
    public boolean isWinnersCommand(){
        return this.type == Type.WINNERS;
    }
    
    public boolean isQuitCommand(){
        return this.type == Type.QUIT;
    }
       
    public Optional<List<String>> getArguments(){
        return this.args;
    }
            
    
    
    /**
     * a helper method to map string input to a known commands
     * unknown input will return empty
     * @param input
     * @return optionally returns a command if a match is found
     */
    public static Optional<Command> fromInput(String input)
    {
        if(input == null || input.trim().isEmpty()){
            return Optional.empty();
        }

        List<String> cmdAndArgs = Arrays.asList(input.trim().toUpperCase().split("\\s+"));
        for(Type type : Type.values())
        {
            String cmd = cmdAndArgs.get(0);
            if(type.getText().equals(cmd))
            {
                if(cmdAndArgs.size() == 1 ){
                    return Optional.of(new Command(type, null, cmd));
                }
                else{
                    return Optional.of( new Command(type,cmdAndArgs.subList(1,cmdAndArgs.size()), cmd));
                }
            }
        }
        return Optional.empty();
    }
}

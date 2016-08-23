package code.aha.lottery.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 *
 * @author aha
 */
public class DefaultInputHandler implements InputHandler
{
    BufferedReader mIn;
    public DefaultInputHandler()
    {
        mIn = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public Optional<Command> next() throws IOException
    {
        return Command.fromInput(mIn.readLine());
    }
    
}

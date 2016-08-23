package code.aha.lottery.input;

import java.io.IOException;
import java.util.Optional;

public interface InputHandler 
{
	Optional<Command> next() throws IOException;
}

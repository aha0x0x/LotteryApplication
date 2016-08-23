package code.aha.lottery;

import code.aha.lottery.draw.DefaultDrawManager;
import code.aha.lottery.draw.DefaultNumberGenerator;
import code.aha.lottery.draw.DrawManager;
import code.aha.lottery.draw.NumberGenerator;
import code.aha.lottery.input.DefaultInputHandler;
import code.aha.lottery.input.InputHandler;

import java.io.PrintStream;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class MainModule extends AbstractModule
{
    @Override
    protected void configure() 
    {
        bind(LotteryManager.class).to(DefaultLotteryManager.class).asEagerSingleton();
        bind(DrawManager.class).to(DefaultDrawManager.class);
        bind(InputHandler.class).to(DefaultInputHandler.class);
        bind(NumberGenerator.class).to(DefaultNumberGenerator.class);
        
        bind(Double.class).annotatedWith(Names.named("INITIAL_POOL")).toInstance(200.0);
        bind(Integer.class).annotatedWith(Names.named("DRAW_RANGE")).toInstance(50);
        bind(PrintStream.class).annotatedWith(Names.named("OUTPUT_STREAM")).toInstance(System.out);
    }
}

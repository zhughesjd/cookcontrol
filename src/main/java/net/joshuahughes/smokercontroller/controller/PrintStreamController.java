package net.joshuahughes.smokercontroller.controller;

import java.io.PrintStream;
import java.util.Map.Entry;

import net.joshuahughes.smokercontroller.Parameters;

public class PrintStreamController implements Controller {
	protected PrintStream printStream;
	public PrintStreamController()
	{
		this(System.out);
	}
	public PrintStreamController(PrintStream printStream) {
		this.printStream = printStream;
	}
	@Override
	public void process(Parameters parameters) {
		for(Entry<Object, Object> entry : parameters.entrySet())
			printStream.println(entry.getKey().toString()+"="+entry.getValue().toString());
		printStream.println("----------------------------");
		for(Entry<Integer, Float> entry : parameters.indexTemperatureMap.entrySet())
			printStream.println("tempIndex"+entry.getKey()+"="+entry.getValue());
		printStream.println("----------------------------");
		printStream.println("Function="+parameters.function.getClass().getSimpleName());
		printStream.println("----------------------------");
		printStream.println("----------------------------");
		printStream.println("----------------------------");
	}
}

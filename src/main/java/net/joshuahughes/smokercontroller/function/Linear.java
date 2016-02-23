package net.joshuahughes.smokercontroller.function;

public class Linear implements Function{

	@Override
	public double normalize(double min, double max, double value) {
		return (value-min)/(max-min);
	}

}

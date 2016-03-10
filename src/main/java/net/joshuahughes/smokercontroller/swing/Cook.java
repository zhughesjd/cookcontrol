package net.joshuahughes.smokercontroller.swing;

import java.util.Random;

import net.joshuahughes.smokercontroller.xml.Cooktype;
import net.joshuahughes.smokercontroller.xml.Thermometertype;

public class Cook extends Parameters<Cooktype,Thermometer>
{
	public Cook(Cooktype type)throws Exception {
		super(type);
	}
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	@Override
	public void init() throws Exception 
	{
		for(Thermometertype child : type.getThermometer())
			children.add(new Thermometer(child, child.getIndex().intValue()));
	}
}

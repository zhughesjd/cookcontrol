package net.joshuahughes.cookcontrol.swing;

import net.joshuahughes.cookcontrol.xml.Cooktype;
import net.joshuahughes.cookcontrol.xml.Thermometertype;

public class Cook extends Element<Cooktype,Thermometer>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public Cook(Cooktype type)throws Exception {
		super(type);
	}
	@Override
	protected void init(Cooktype type) throws Exception 
	{
		for(Thermometertype child : type.getThermometer())
			children.add(new Thermometer(child, child.getIndex().intValue()));
	}
}

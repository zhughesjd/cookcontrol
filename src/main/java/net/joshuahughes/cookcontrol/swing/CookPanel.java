package net.joshuahughes.cookcontrol.swing;

import net.joshuahughes.cookcontrol.data.Cook;
import net.joshuahughes.cookcontrol.data.Thermometer;

public class CookPanel extends DataPanel<Thermometer,Cook,ThermometerPanel>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public CookPanel(Cook type)throws Exception {
		super(type);
	}
	@Override
	protected void init(Cook type) throws Exception 
	{
		for(Thermometer child : type.getChildren())
			children.add(new ThermometerPanel(child, child.getIndex()));
	}
}

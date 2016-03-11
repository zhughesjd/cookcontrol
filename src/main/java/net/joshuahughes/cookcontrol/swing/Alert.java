package net.joshuahughes.cookcontrol.swing;

import net.joshuahughes.cookcontrol.xml.Alerttype;

public class Alert extends Element<Alerttype,Alert>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public Alert(Alerttype type) throws Exception
	{
		super(type);
		remove(getComponentCount()-1);
	}
	@Override
	protected void init(Alerttype type) throws Exception 
	{
	}
}

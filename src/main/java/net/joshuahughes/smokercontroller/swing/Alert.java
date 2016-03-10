package net.joshuahughes.smokercontroller.swing;

import net.joshuahughes.smokercontroller.xml.Alerttype;

public class Alert extends Parameters<Alerttype,String>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public Alert(Alerttype type) throws Exception
	{
		super(type);
		remove(childPanel);
	}
	@Override
	public void init()
	{
	}
}

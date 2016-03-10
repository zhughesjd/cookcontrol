package net.joshuahughes.smokercontroller.parameters;

import net.joshuahughes.smokercontroller.xml.Alerttype;

public class Alert extends Parameters<Alerttype,String>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public Alert(Alerttype type) throws Exception
	{
		super(type);
	}
	@Override
	public void init() {
		remove(this.childPanel);
	}
}

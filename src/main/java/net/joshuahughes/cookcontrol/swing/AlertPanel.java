package net.joshuahughes.cookcontrol.swing;

import net.joshuahughes.cookcontrol.data.Alert;

public class AlertPanel extends DataPanel<Alert,Alert,AlertPanel>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public AlertPanel(Alert type) throws Exception
	{
		super(type);
		remove(getComponentCount()-1);
	}
	@Override
	protected void init(Alert type) throws Exception 
	{
		
	}
}

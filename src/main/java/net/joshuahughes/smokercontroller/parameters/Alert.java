package net.joshuahughes.smokercontroller.parameters;

import java.io.File;

public class Alert extends Parameters<String>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public Alert(File directory) throws Exception
	{
		super(directory);
	}
	@Override
	public void init() {
		remove(this.childPanel);
	}
}

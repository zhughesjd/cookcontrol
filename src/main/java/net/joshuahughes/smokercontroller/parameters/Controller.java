package net.joshuahughes.smokercontroller.parameters;

import java.util.Random;

import javax.swing.JDialog;

public class Controller extends Parameters<Smoke>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	public Controller()
	{
		put(StringKey.macaddress,getMACAddress());
		initialize();
	}
	@Override
	public boolean addChildOperations() {
		return false;
	}
	@Override
	public Smoke createChild() {
		return null;
	}
	public static void main(String[] args)
	{
		JDialog dlg = new JDialog();
		dlg.setContentPane(new Controller());
		dlg.setSize(500,500);
		dlg.setVisible(true);
	}
}

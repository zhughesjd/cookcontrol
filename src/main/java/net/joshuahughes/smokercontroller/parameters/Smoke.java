package net.joshuahughes.smokercontroller.parameters;

import java.util.Random;

import javax.swing.JDialog;

public class Smoke extends Parameters<Thermometer>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	public Smoke()
	{
		initialize();
	}
	@Override
	public boolean addChildOperations() {
		return false;
	}
	@Override
	public Thermometer createChild() {
		return null;
	}
	@Override
	public void init() {
		
	}
	public static void main(String[] args)
	{
		JDialog dlg = new JDialog();
		dlg.setContentPane(new Smoke());
		dlg.setSize(500,500);
		dlg.setVisible(true);
	}
}

package net.joshuahughes.smokercontroller.parameters;

import java.util.Random;

import javax.swing.JDialog;

public class App extends Parameters<Controller>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	public App()
	{
		initialize();
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean addChildOperations() {
		return false;
	}
	@Override
	public Controller createChild() {
		return null;
	}
	public static void main(String[] args)
	{
		JDialog dlg = new JDialog();
		dlg.setContentPane(new App());
		dlg.setSize(500,500);
		dlg.setVisible(true);
	}
}

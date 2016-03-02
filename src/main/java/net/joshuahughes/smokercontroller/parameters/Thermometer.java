package net.joshuahughes.smokercontroller.parameters;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JDialog;

public class Thermometer extends Parameters<Alert>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	public Thermometer(int index)
	{
		
		candidateLabels.addAll(Arrays.asList("probe ","ambient ","beef ","chicken ","pork "));
		put(IntKey.index,index);
		putComponent(StringKey.color,Parameters.getString(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256))));
		putComponent(StringKey.label,index<0?"sensor":"probe "+index);
		initialize();
	}
	@Override
	public boolean addChildOperations() {
		return true;
	}
	@Override
	public Alert createChild() {
		return new Alert();
	}
	public static void main(String[] args)
	{
		JDialog dlg = new JDialog();
		dlg.setContentPane(new Thermometer(1));
		dlg.setSize(500,500);
		dlg.setVisible(true);
	}
}

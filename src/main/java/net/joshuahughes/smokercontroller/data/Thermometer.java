package net.joshuahughes.smokercontroller.data;

import java.awt.Color;
import java.util.Random;

import javax.swing.JOptionPane;

import net.joshuahughes.smokercontroller.enumproperties.EnumProperties;

public class Thermometer extends Parameters<Alert>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	private int index = Integer.MIN_VALUE;
	public Thermometer(int index)
	{
		this.index = index;
		put(StringKey.color,EnumProperties.getString(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256))));
		put(StringKey.label,index<0?"sensor":"probe "+index);
		initialize();
	}
	public int getIndex()
	{
		return index;
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
		JOptionPane.showInputDialog(new Thermometer(1));
	}
}

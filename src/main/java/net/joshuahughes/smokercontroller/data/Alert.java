package net.joshuahughes.smokercontroller.data;

import java.awt.Color;

import javax.swing.JOptionPane;

import net.joshuahughes.smokercontroller.enumproperties.EnumProperties;

public class Alert extends Parameters<String>
{
	private static final long serialVersionUID = 3857793667006092846L;
	private static int idIncr = 0;
	public Alert()
	{
		put(StringKey.label,Alert.class.getSimpleName().toLowerCase()+" "+idIncr++);
		put(StringKey.email,"");
		put(FloatKey.maxtemperature,500f,Float.NEGATIVE_INFINITY,Float.POSITIVE_INFINITY,1f);
		put(StringKey.color, EnumProperties.getString(Color.red));
		put(BooleanKey.light, true);
		put(BooleanKey.sound, true);
		put(BooleanKey.vibrate, true);
		initialize();
		remove(this.childPanel);
	}
	@Override
	public boolean addChildOperations() {
		return false;
	}
	@Override
	public String createChild() {
		return "";
	}
	public static void main(String[] args)
	{
		JOptionPane.showInputDialog(new Alert());
	}
}

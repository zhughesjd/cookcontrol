package net.joshuahughes.smokercontroller.parameters;

import java.awt.Color;

import javax.swing.JDialog;

import net.joshuahughes.smokercontroller.enumproperties.EnumProperties;

public class Alert extends Parameters<String>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public Alert()
	{
		putComponent(StringKey.email,"");
		putComponent(FloatKey.maxtemperature,500f,Float.NEGATIVE_INFINITY,Float.POSITIVE_INFINITY,1f);
		putComponent(StringKey.color, EnumProperties.getString(Color.red));
		putComponent(BooleanKey.light, true);
		putComponent(BooleanKey.sound, true);
		putComponent(BooleanKey.vibrate, true);
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
		JDialog dlg = new JDialog();
		dlg.setContentPane(new Alert());
		dlg.setSize(500,500);
		dlg.setVisible(true);
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}

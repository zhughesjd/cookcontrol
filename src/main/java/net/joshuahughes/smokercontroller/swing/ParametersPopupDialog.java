package net.joshuahughes.smokercontroller.swing;

import javax.swing.JDialog;
import javax.xml.bind.JAXB;

import net.joshuahughes.smokercontroller.xml.Cooktype;

public class ParametersPopupDialog{
	public static void main(String[] args) throws Exception
	{
		JDialog dlg = new JDialog(); 
		Cook cook = new Cook(JAXB.unmarshal(ParametersPopupDialog.class.getResourceAsStream("/example.xml"), Cooktype.class));
		dlg.setContentPane(cook);
		dlg.setSize(1000, 1000);
		dlg.setVisible(true);
	}
}

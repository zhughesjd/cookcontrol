package net.joshuahughes.smokercontroller.swing;

import javax.swing.JDialog;
import javax.xml.bind.JAXB;

import net.joshuahughes.smokercontroller.xml.Cooktype;

public class ParametersPopupDialog extends JDialog{
	private static final long serialVersionUID = 3264848822760404672L;
	JDialog smokeDialog = new JDialog(this);
	int width = 500;
	int height = 500;
	public ParametersPopupDialog() throws Exception
	{
		Cook cook = new Cook(JAXB.unmarshal(getClass().getResourceAsStream("/example.xml"), Cooktype.class));
		setContentPane(cook);
		setSize(width, height);
		smokeDialog.setSize(width, height);
	}
	public static void main(String[] args) throws Exception
	{
		ParametersPopupDialog dlg = new ParametersPopupDialog();
		dlg.setVisible(true);
	}
}

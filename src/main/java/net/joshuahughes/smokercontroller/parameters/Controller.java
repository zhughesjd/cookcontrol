package net.joshuahughes.smokercontroller.parameters;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

public class Controller extends Parameters<Smoke>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	@Override
	public void init() {
		put(StringKey.macaddress,getMACAddress());
	}
	@Override
	public boolean addChildOperations() {
		return true;
	}
	@Override
	public Smoke createChild() {
		Smoke smoke = new Smoke();
		smoke.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				Controller.this.validate();
				Controller.this.repaint();
			}
		});
		return smoke;
	}
	public static void main(String[] args)
	{
		try {
			URL url = new URL("http://div1palehorse.metsci.com/ais/");
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(url.openStream()));

			        String inputLine;
			        while ((inputLine = in.readLine()) != null)
			            System.out.println(inputLine);
			        in.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		JDialog dlg = new JDialog();
//		dlg.setTitle(Controller.class.getSimpleName());
//		dlg.setContentPane(new Controller());
//		dlg.setSize(500,500);
//		dlg.setVisible(true);
	}
}

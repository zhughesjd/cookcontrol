package net.joshuahughes.cookcontrol.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.xml.bind.JAXB;

import net.joshuahughes.cookcontrol.xml.Cooktype;

public class SwingApplication{
	public static void main(String[] args) throws Exception
	{
		JDialog dlg = new JDialog(); 
		Cook cook = new Cook(JAXB.unmarshal(SwingApplication.class.getResourceAsStream("/example.xml"), Cooktype.class));
		cook.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event)
			{
				Element<?,?> element = (Element<?, ?>) event.getSource();
				System.out.println(element.getClass());
				JDialog dlg = new JDialog(); 
				dlg.setContentPane(element);
				dlg.setSize(1000, 1000);
				dlg.setVisible(true);
			}
		});
		dlg.setContentPane(cook);
		dlg.setSize(1000, 1000);
		dlg.setVisible(true);
	}
}

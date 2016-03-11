package net.joshuahughes.cookcontrol.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.xml.bind.JAXB;

import net.joshuahughes.cookcontrol.xml.Cooktype;

@SuppressWarnings("unchecked")
public class SwingApplication{
	public static void main(String[] args) throws Exception
	{
		singleDialog();
	}
	public static void singleDialog() throws Exception
	{
		Cook cook = new Cook(JAXB.unmarshal(SwingApplication.class.getResourceAsStream("/example.xml"), Cooktype.class));
		JDialog dlg = new JDialog();
		dlg.setContentPane(new JPanel(new BorderLayout()));
		JMenuBar menuBar = new JMenuBar();
		ArrayList<Element<?,?>> generationList = new ArrayList<Element<?,?>>();
		JButton parentButton = new JButton(new AbstractAction("") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				if(generationList.isEmpty())return;
				JButton button = (JButton)e.getSource();
				Element<?, ?> parent = generationList.remove(generationList.size()-1);
				String grandParentname = generationList.size()>0?generationList.get(generationList.size()-1).toString():"";
				button.setText(grandParentname);
				dlg.setContentPane(parent);
				if(generationList.isEmpty())
					menuBar.remove(button);
				dlg.validate();
			}
		});
		dlg.setJMenuBar(menuBar);
		dlg.getContentPane().add(cook,BorderLayout.CENTER);
		cook.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount()>=2)
				{
					Element<?, ?> parent = (Element<?, ?>) dlg.getContentPane();
					generationList.add( parent );
					if(!Arrays.asList(menuBar.getComponents()).contains(parentButton))
						menuBar.add(parentButton);
					parentButton.setText("<-"+parent.toString());
					Element<?,?> childElement = ((JList<Element<?,?>>)event.getSource()).getSelectedValue();
					if(childElement == null) return;
					dlg.setContentPane(childElement);
					dlg.validate();
				}
			}
		});
		dlg.setContentPane(cook);
		dlg.setSize(1000, 500);
		dlg.setVisible(true);
	}
	public static void multipleDialogs(String xmlFilepath) throws Exception
	{
		JDialog dlg = new JDialog();
		Cook cook = new Cook(JAXB.unmarshal(SwingApplication.class.getResourceAsStream(xmlFilepath), Cooktype.class));
		cook.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent event)
			{
				if(event.getClickCount()>=2)
				{
					Element<?,?> element = ((JList<Element<?,?>>)event.getSource()).getSelectedValue();
					JDialog dlg = new JDialog();
					dlg.setContentPane(element);
					dlg.setSize(1000, 1000);
					dlg.setVisible(true);
				}
			}
		});
		dlg.setContentPane(cook);
		dlg.setSize(1000, 1000);
		dlg.setVisible(true);
	}
}

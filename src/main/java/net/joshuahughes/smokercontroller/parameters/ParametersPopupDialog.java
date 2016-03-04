package net.joshuahughes.smokercontroller.parameters;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ParametersPopupDialog extends JDialog{
	private static final long serialVersionUID = 3264848822760404672L;
//	{
//		private static final long serialVersionUID = -5558111443516803699L;
//		public Smoke createChild()
//		{
//			Smoke smoke  = super.createChild();
//			for(int index=-1;index<12;index+=1+random.nextInt(3))
//			{
//				smoke.getChildPanel().getChildList().add( new Thermometer(index));
//			}
//			return smoke;
//		}
//	}
	;
	JDialog smokeDialog = new JDialog(this);
	int width = 500;
	int height = 500;
	public ParametersPopupDialog() throws Exception
	{
		SmokerController controller = new SmokerController(new File(""));
		setContentPane(controller);
		setSize(width, height);
		smokeDialog.setSize(width, height);
		controller.getChildPanel().getChildList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) return;
				Smoke smoke = (Smoke) ((JList<?>)e.getSource()).getSelectedValue();
				boolean visibility = false;
				if(smoke != null)
				{
					visibility = true;
					smokeDialog.setContentPane(smoke);
					smokeDialog.setTitle(smoke.toString());
				}
				smokeDialog.setVisible(visibility);
			}
		});
	}
	public static void main(String[] args) throws Exception
	{
		ParametersPopupDialog dlg = new ParametersPopupDialog();
		dlg.setVisible(true);
	}
}

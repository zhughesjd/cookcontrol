package net.joshuahughes.smokercontroller.enumproperties;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map.Entry;

import javax.swing.JComponent;

public class TemperatureAlert extends EnumProperties
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2908714710835616733L;
	private static int idIncr = 0;
	public TemperatureAlert()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=gbc.gridy=0;
		gbc.weightx=gbc.weighty=1;
		put(StringKey.label,"alarm "+idIncr++);
		put(StringKey.email,"");
		put(FloatKey.maxtemperature,500);
		put(BooleanKey.light, true);
		put(BooleanKey.sound, true);
		put(BooleanKey.vibrate, true);
		panel.setLayout(new GridBagLayout());
		for(Entry<Object, Object> entry : entrySet())
		{
			if(entry.getKey() instanceof Key)
			{
				if(entry.getKey().toString().equals(IntKey.probeindex.toString())) continue;
				JComponent cmp = getComponent(this, (Key<?>) entry.getKey(), entry.getValue());
				if(cmp!=null)
				{
					panel.add(cmp,gbc);
					gbc.gridy++;
				}
			}
		}
	}
	public String toString()
	{
		return get(StringKey.label);
	}
	
}

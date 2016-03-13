package net.joshuahughes.cookcontrol.swing;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import net.joshuahughes.cookcontrol.data.Alert;
import net.joshuahughes.cookcontrol.data.Thermometer;
import net.joshuahughes.cookcontrol.data.property.StringProperty.StringKey;

public class ThermometerPanel extends DataPanel<Alert,Thermometer,AlertPanel>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	public ThermometerPanel(Thermometer type,int index) throws Exception
	{
		super(type,Arrays.asList(new String[]{"probe","ambient","beef","chicken","pork","fish"}).stream().map(i->i+" "+index).collect(Collectors.toList()).toArray(new String[0]));
		putComponent(StringKey.label,index<0?"sensor":"probe "+index);
	}
	@Override
	protected void init(Thermometer type) throws Exception 
	{
		for(Alert child : type.getChildren())
			children.add(new AlertPanel(child));
		putComponent(StringKey.color,DataPanel.getString(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256))));
	}
}

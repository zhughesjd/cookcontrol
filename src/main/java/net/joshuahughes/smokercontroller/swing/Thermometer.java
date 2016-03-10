package net.joshuahughes.smokercontroller.swing;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import net.joshuahughes.smokercontroller.xml.Alerttype;
import net.joshuahughes.smokercontroller.xml.Thermometertype;

public class Thermometer extends Parameters<Thermometertype,Alert>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	public Thermometer(Thermometertype type,int index) throws Exception
	{
		super(type,Arrays.asList(new String[]{"probe","ambient","beef","chicken","pork","fish"}).stream().map(i->i+" "+index).collect(Collectors.toList()).toArray(new String[0]));
		putComponent(StringKey.label,index<0?"sensor":"probe "+index);
	}
	@Override
	public void init() throws Exception 
	{
		for(Alerttype child : type.getAlert())
			children.add(new Alert(child));
		putComponent(StringKey.color,Parameters.getString(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256))));
	}
}

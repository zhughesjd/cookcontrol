package net.joshuahughes.smokercontroller.parameters;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Thermometer extends Parameters<Alert>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	public Thermometer(File file,int index) throws Exception
	{
		super(file,Arrays.asList(new String[]{"probe","ambient","beef","chicken","pork","fish"}).stream().map(i->i+" "+index).collect(Collectors.toList()).toArray(new String[0]));
	}
	@Override
	public void init() {
		putComponent(StringKey.color,Parameters.getString(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256))));
		putComponent(StringKey.label,idIncr<0?"sensor":"probe "+idIncr);
		idIncr++;
	}
}

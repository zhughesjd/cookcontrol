package net.joshuahughes.smokercontroller.parameters;

import java.util.Random;

import net.joshuahughes.smokercontroller.xml.Smoketype;

public class Smoke extends Parameters<Smoketype,Thermometer>
{
	public Smoke(Smoketype type)throws Exception {
		super(type);
	}
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	@Override
	public void init() {
		
	}
}

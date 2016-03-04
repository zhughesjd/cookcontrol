package net.joshuahughes.smokercontroller.parameters;

import java.io.File;
import java.util.Random;

public class Platform extends Parameters<Smoke>
{
	public Platform(File directory)throws Exception {
		super(directory);
	}
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	@Override
	public void init() {
		put(StringKey.macaddress,getMACAddress());
	}
}

package net.joshuahughes.smokercontroller.parameters;

import java.io.File;
import java.util.Random;

public class SmokerController extends Parameters<Platform>
{
	public SmokerController(File directory) throws Exception {
		super(directory);
		File thisPlatformFile = new File(directory.getCanonicalPath()+File.separatorChar+SmokerController.getMACAddress());
	}
	private static final long serialVersionUID = 3857793667006092846L;
	public static Random random = new Random(34234928374l);
	@Override
	public void init()
	{
	}
}

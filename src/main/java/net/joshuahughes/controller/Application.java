package net.joshuahughes.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.pi4j.wiringpi.Spi;


public class Application {

	static List<String> faults = new ArrayList<String>();
	public static String macAddress = getMACAddress();
	public static String homePath = System.getProperty("user.home");
	public static String gitDirectoryPath = homePath+"\\smokercontroller\\";
	public static String branch = "gh-pages";
	public static void main(String[] args) throws Exception {
		File gitDirectory = new File(gitDirectoryPath);
		Git git = null;
		if(!gitDirectory.exists())
		{
			git = Git.cloneRepository()
					.setURI( "https://github.com/zhughesjd/smokercontroller.git" )
					.setDirectory( gitDirectory )
					.setBranchesToClone( Collections.singleton( branch ) )
					.setBranch( branch )
					.call();
		}
		else
		{
			git = Git.open(gitDirectory);
			git.pull();
		}
		File propertiesFile = new File(gitDirectory+"\\"+macAddress+"\\properties.txt");
		if(!propertiesFile.exists())
		{
			propertiesFile.getParentFile().mkdirs();
			BufferedWriter bw = new BufferedWriter(new FileWriter(propertiesFile));
			bw.write("simpleName=controller");
			bw.close();
			git.commit().setMessage("initial macs").call();
			git.push().setForce(true).setCredentialsProvider( new UsernamePasswordCredentialsProvider( "zhughesjd", "manred9" ));
		}
		
		System.exit(1);
		// https://projects.drogon.net/understanding-spi-on-the-raspberry-pi/
		// http://developer-blog.net/wp-content/uploads/2013/09/raspberry-pi-rev2-gpio-pinout.jpg
		// http://en.wikipedia.org/wiki/Serial_Peripheral_Interface_Bus
		int channel = Spi.CHANNEL_0;
		int fd = Spi.wiringPiSPISetup(channel, 500000); // 500 kHz
		if (fd == -1) {
			throw new RuntimeException("SPI setup failed.");
		}
		// http://pi4j.com/example/control.html
		MAX31855 max31855 = new MAX31855(channel);

		int[] raw = new int[2];
		while (true) {
			for(int index=0;index<8;index++){
				int faults = max31855.readRaw(raw,index);

				float internal = max31855.getInternalTemperature(raw[0]);
				float thermocouple = max31855.getThermocoupleTemperature(raw[1]);

				System.out.println("Internal = " + internal + " F, Thermocouple = " + thermocouple + " F");
				if (faults != 0) {
					onFaults(faults);
				}
			}
		}
	}

	private static void onFaults(int f) {
		faults.clear();

		if ((f & MAX31855.FAULT_OPEN_CIRCUIT_BIT) == MAX31855.FAULT_OPEN_CIRCUIT_BIT)
			faults.add("Open Circuit");
		if ((f & MAX31855.FAULT_SHORT_TO_GND_BIT) == MAX31855.FAULT_SHORT_TO_GND_BIT)
			faults.add("Short To GND");
		if ((f & MAX31855.FAULT_SHORT_TO_VCC_BIT) == MAX31855.FAULT_SHORT_TO_VCC_BIT)
			faults.add("Short To VCC");

		boolean first = true;
		String text = "Faults = ";
		for (String fault : faults) {
			if (!first)
				text += ", ";
			text += fault;
		}

		System.err.println(text);
	}
	public static final String getMACAddress()
	{
		try {
			NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
			}
			return sb.toString();
		} catch (Exception e)
		{
		}
		return "invalid";
	}

}

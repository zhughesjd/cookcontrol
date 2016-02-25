package net.joshuahughes.smokercontroller.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import net.joshuahughes.smokercontroller.Parameters;

public class GitHubController implements Controller{
	public static String macAddress = getMACAddress();
	public static String homePath = System.getProperty("user.home");
	public static String gitDirectoryPath = homePath+"\\smokercontroller\\";
	public static String branch = "gh-pages";
	public GitHubController() throws Exception
	{
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
			git.add().addFilepattern(macAddress).call();
			git.commit().setAll(true).setMessage("test commit").call();
			git.push().setCredentialsProvider( new UsernamePasswordCredentialsProvider( "zhughesjd", "manred9" )).call();
		}

	}
	@Override
	public void process(Parameters input) {
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

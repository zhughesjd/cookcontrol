package net.joshuahughes.smokercontroller.controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.joshuahughes.smokercontroller.Parameters;

public class SwingController extends PrintStreamController {
	static ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
	JPanel controlPanel = new JPanel();
	JPanel graphPanel = new JPanel();
	public JTextArea textArea = new JTextArea();
	public SwingController() {
		super(new PrintStream(baos));
		JDialog dlg = new JDialog();
		Container content = dlg.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(new JScrollPane(controlPanel), BorderLayout.WEST);
		content.add(new JScrollPane(graphPanel), BorderLayout.CENTER);
		content.add(new JScrollPane(textArea), BorderLayout.EAST);
		dlg.setSize(900, 300);
		dlg.setVisible(true);
	}
	@Override
	public void process(Parameters parameters) {
		super.process(parameters);
		try {
			baos.flush();
			textArea.setText(baos.toString());
			baos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

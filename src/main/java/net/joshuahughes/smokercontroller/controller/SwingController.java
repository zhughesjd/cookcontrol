package net.joshuahughes.smokercontroller.controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import net.joshuahughes.smokercontroller.Parameters;
import net.joshuahughes.smokercontroller.Parameters.FloatKey;
import net.joshuahughes.smokercontroller.Parameters.IntKey;
import net.joshuahughes.smokercontroller.Parameters.Key;
import net.joshuahughes.smokercontroller.Parameters.LongKey;

public class SwingController extends PrintStreamController {
	static ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
	Key<?>[] controllableKeys = new Key<?>[]{LongKey.sleep,FloatKey.lotemperature,FloatKey.temperaturerange,IntKey.fantemperatureindex};
	LinkedHashMap<Key<?>,SpinnerNumberModel> modelMap = new LinkedHashMap<>();
	JPanel controlPanel = new JPanel(new GridBagLayout());
	JPanel graphPanel = new TemperatureChart();
	public JTextArea textArea = new JTextArea();
	public SwingController(Parameters parameters) {
		super(new PrintStream(baos));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = gbc.gridy = 0;
		for(Key<?> key : controllableKeys)
		{
			gbc.gridx = 0;
			controlPanel.add(new JLabel(key.toString()+": "),gbc);
			Number number = (Number) parameters.get(key);
			if(number == null)
				number = key instanceof FloatKey ?1d:1;
			SpinnerNumberModel model = new SpinnerNumberModel(number instanceof Float?number.doubleValue():number.intValue(),0,30000,1);
			modelMap.put(key, model);
			gbc.gridx++;
			controlPanel.add(new JSpinner(model),gbc);
			gbc.gridy++;
		}
		JDialog dlg = new JDialog();
		Container content = dlg.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(new JScrollPane(controlPanel), BorderLayout.WEST);
		content.add(new JScrollPane(graphPanel), BorderLayout.CENTER);
		content.add(new JScrollPane(textArea), BorderLayout.EAST);
		dlg.setSize(1000, 500);
		dlg.setVisible(true);
	}
	@Override
	public void process(Parameters parameters) {
		super.process(parameters);
		for(Entry<Key<?>, SpinnerNumberModel> entry : modelMap.entrySet())
		{
			Number value = ((Number)entry.getValue().getValue()).intValue();
			if(entry.getKey() instanceof LongKey)
				value = ((Number)entry.getValue().getValue()).longValue();
			if(entry.getKey() instanceof FloatKey)
				value = ((Number)entry.getValue().getValue()).floatValue();
			parameters.put(entry.getKey(),value);
		}
		try {
			baos.flush();
			textArea.setText(baos.toString());
			baos.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

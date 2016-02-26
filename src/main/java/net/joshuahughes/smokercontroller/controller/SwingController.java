package net.joshuahughes.smokercontroller.controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import net.joshuahughes.smokercontroller.Parameters;
import net.joshuahughes.smokercontroller.Parameters.FloatKey;
import net.joshuahughes.smokercontroller.Parameters.IntKey;
import net.joshuahughes.smokercontroller.Parameters.Key;
import net.joshuahughes.smokercontroller.Parameters.LongKey;

public class SwingController extends PrintStreamController {
	public static float maxValidTemperature = 900;
	public static String sensorSeriesId = "Sensor";
	public static String seriesKey = "SeriesKey";
	static ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
	Key<?>[] controllableKeys = new Key<?>[]{LongKey.sleep,FloatKey.lotemperature,FloatKey.temperaturerange,IntKey.fantemperatureindex};
	LinkedHashMap<Key<?>,SpinnerNumberModel> modelMap = new LinkedHashMap<>();
	JPanel controlPanel = new JPanel(new GridBagLayout());
	JTextArea textArea = new JTextArea();
	TimeSeriesCollection dataset = new TimeSeriesCollection();
	JPanel boxPanel = new JPanel();
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
		JFrame frame = new JFrame();
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(new JScrollPane(controlPanel), BorderLayout.WEST);
		ChartPanel chartPanel = new ChartPanel(ChartFactory.createTimeSeriesChart("All Probes", "Time","Temperature",dataset,true,true,false));
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(chartPanel, BorderLayout.CENTER);
		boxPanel.setLayout(new BoxLayout(boxPanel,BoxLayout.Y_AXIS));
		centerPanel.add(boxPanel, BorderLayout.WEST);
		content.add(centerPanel, BorderLayout.CENTER);
		content.add(new JScrollPane(textArea), BorderLayout.EAST);
		frame.setSize(1000, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	@Override
	public void process(Parameters parameters) {
		super.process(parameters);
		addToChart(parameters);
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
	private void addToChart(Parameters parameters) {
		System.out.println("---------------");
		Second second  = new Second(new Date(parameters.get(LongKey.utctime)), TimeZone.getTimeZone("EST"), Locale.ENGLISH);
		for(Entry<Integer, Float> entry : parameters.probeTemperatures.entrySet())
			if(entry.getValue() < maxValidTemperature)
			{
				Float temp = entry.getValue();
				if(temp<maxValidTemperature)
					getTimeSeries(entry.getKey()).add(second, temp, true);
			}
		Float temp = parameters.get(FloatKey.sensortemperature);
		if(temp !=null && temp < maxValidTemperature)
			getTimeSeries(-1).add(second, temp, true);
	}
	private TimeSeries getTimeSeries(int index) {
		String id = index<0 ?"sensor": "probe "+index;
		JCheckBox box = null;
		for(Component component : boxPanel.getComponents())
			if(component.getName().equals(id))
				box = (JCheckBox) component;
		if(box == null)
		{
			box = new JCheckBox()
			{
				private static final long serialVersionUID = 1L;
				public String getName()
				{
					return ((TimeSeries)getClientProperty(sensorSeriesId)).getKey().toString();
				}
				public String getText()
				{
					if(getClientProperty(sensorSeriesId) == null) return "";
					return ((TimeSeries)getClientProperty(sensorSeriesId)).getKey().toString();
				}
			};
			TimeSeries series = new TimeSeries(id);
			box.putClientProperty(sensorSeriesId, series);
			boxPanel.add(box);
			box.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
						dataset.addSeries(series);
					else
						dataset.removeSeries(series);
				}
			});
			box.setSelected(false);
			box.doClick();
			boxPanel.revalidate();
		}
		return (TimeSeries)box.getClientProperty(sensorSeriesId);
	}
}

package net.joshuahughes.smokercontroller.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import net.joshuahughes.smokercontroller.enumproperties.EnumProperties.FloatKey;
import net.joshuahughes.smokercontroller.enumproperties.EnumProperties.IntKey;
import net.joshuahughes.smokercontroller.enumproperties.EnumProperties.Key;
import net.joshuahughes.smokercontroller.enumproperties.EnumProperties.LongKey;
import net.joshuahughes.smokercontroller.enumproperties.EnumProperties.StringKey;
import net.joshuahughes.smokercontroller.enumproperties.Parameters;
import net.joshuahughes.smokercontroller.enumproperties.Thermometer;

public class SwingController extends PrintStreamController {
	public static float maxValidTemperature = 900;
	public static String sensorSeriesId = "Sensor";
	public static String seriesKey = "SeriesKey";
	static ByteArrayOutputStream baos = new ByteArrayOutputStream(3000);
	Key<?>[] controllableKeys = new Key<?>[]{LongKey.sleep,FloatKey.mintemperature,FloatKey.maxtemperature,IntKey.fantemperatureindex};
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
		Second second  = new Second(new Date(parameters.get(LongKey.utctime)), TimeZone.getTimeZone("EST"), Locale.ENGLISH);
		for(Entry<Thermometer, Float> entry : parameters.getTemperatureEntries())
			if(entry.getValue() < maxValidTemperature)
			{
				Float temp = entry.getValue();
				if(temp<maxValidTemperature)
					getTimeSeries(entry.getKey()).add(second, temp, true);
			}
	}
	private TimeSeries getTimeSeries(Thermometer feature) {
		JCheckBoxButton box = null;
		for(Component component : boxPanel.getComponents())
			if(component.getName().equals(feature.toString()))
				box = (JCheckBoxButton) component;
		if(box == null)
		{
			box = new JCheckBoxButton(new TimeSeries(feature));
			boxPanel.add(box);
			boxPanel.validate();
		}
		return (TimeSeries)box.getClientProperty(sensorSeriesId);
	}
	public class JCheckBoxButton extends JPanel
	{
		private static final long serialVersionUID = 1L;
		TimeSeries ts;
		JLabel label = new JLabel(){
			private static final long serialVersionUID = 1L;
			public String getText(){
				invalidate();
				return ts == null ?"":ts.getKey().toString();
			}
		};
		JCheckBox box = new JCheckBox("",true);
		public JCheckBoxButton(TimeSeries ts)
		{
			super(new BorderLayout());
			putClientProperty(sensorSeriesId, ts);
			this.ts = ts;
			add(box,BorderLayout.WEST);
			add(label,BorderLayout.CENTER);
			label.setText(ts.getKey().toString());
			box.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(box.isSelected())
						dataset.addSeries(ts);
					else
						dataset.removeSeries(ts);
				}
			});
			box.setSelected(false);
			box.doClick();
			label.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e)
				{
					JDialog dlg = new JDialog();
					dlg.setContentPane(new EditorPanel(ts));
					dlg.setVisible(true);
					dlg.setSize(300,300);
				}
			});
		}
		public String getName()
		{
			return ts.getKey().toString();
		}
	}
	public class EditorPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public EditorPanel(TimeSeries ts)
		{
			Thermometer thermometer = (Thermometer) ts.getKey();
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx=gbc.gridy=0;
			gbc.weightx=gbc.weighty=1;
			for(Entry<Object, Object> entry : thermometer.entrySet())
				if(entry.getKey() instanceof Key)
				{
					JComponent cmp = null;
					Key<?> key = (Key<?>) entry.getKey();
					Object value = entry.getValue();
					if(value instanceof Integer)
					{
						JPanel panel = new JPanel(new BorderLayout());
						cmp = panel;
						panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
						SpinnerNumberModel model = new SpinnerNumberModel((int)value, 0, 2000, 1);
						panel.add(new JSpinner(model ),BorderLayout.CENTER);
					}
					if(value instanceof String)
					{
						JPanel panel = new JPanel(new BorderLayout());
						cmp = panel;
						panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
						JTextField field = new JTextField(key.toString());
						panel.add(field,BorderLayout.CENTER);
						field.setPreferredSize(new Dimension(100, 24));
						field.setText(value.toString());
						if(key.toString().equals(StringKey.label.toString()))
							field.addFocusListener(new FocusListener() {
								
								@Override
								public void focusLost(FocusEvent e)
								{
									thermometer.setLabel(field.getText());
									field.setText(thermometer.get(StringKey.label));
									SwingUtilities.getWindowAncestor(boxPanel).validate();
								}
								
								@Override
								public void focusGained(FocusEvent e) {
									
								}
							});
					}
					if(cmp!=null)
					{
						add(cmp,gbc);
						gbc.gridy++;
					}
				}
		}
	}
	public class HintTextField extends JTextField {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public HintTextField(String hint) {
	        _hint = hint;
	    }
	    @Override
	    public void paint(Graphics g) {
	        super.paint(g);
	        if (getText().length() == 0) {
	            int h = getHeight();
	            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	            Insets ins = getInsets();
	            FontMetrics fm = g.getFontMetrics();
	            int c0 = getBackground().getRGB();
	            int c1 = getForeground().getRGB();
	            int m = 0xfefefefe;
	            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
	            g.setColor(new Color(c2, true));
	            g.drawString(_hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
	        }
	    }
	    private final String _hint;
	}
}

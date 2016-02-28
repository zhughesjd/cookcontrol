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
import java.awt.event.FocusAdapter;
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

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
import net.joshuahughes.smokercontroller.enumproperties.TemperatureAlert;
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
	JPanel boxPanel = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	ChartPanel chartPanel = new ChartPanel(ChartFactory.createTimeSeriesChart("All Probes", "Time","Temperature",dataset,true,true,false));
	public SwingController(Parameters parameters) {
		super(new PrintStream(baos));
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
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		controlPanel.add(new JButton(new AbstractAction("comments"){
			private static final long serialVersionUID = -5989081975520668735L;
			@Override
			public void actionPerformed(ActionEvent e) {
				comment(parameters.timeCommentMap);
				
			}}),gbc);
		JFrame frame = new JFrame();
		frame.setTitle("Smoker Controller");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(new JScrollPane(controlPanel), BorderLayout.WEST);
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(chartPanel, BorderLayout.CENTER);
		centerPanel.add(boxPanel, BorderLayout.WEST);
		content.add(centerPanel, BorderLayout.CENTER);
		content.add(new JScrollPane(textArea), BorderLayout.EAST);
		frame.setSize(1000, 500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gbc.gridx=gbc.gridy=0;
		gbc.weightx=gbc.weighty=1;
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
			boxPanel.add(box,gbc);
			gbc.gridy++;
			boxPanel.validate();
		}
		return (TimeSeries)box.getClientProperty(sensorSeriesId);
	}
	public class JCheckBoxButton extends JPanel
	{
		private static final long serialVersionUID = 1L;
		TimeSeries ts;
		JLabel label = new JLabel(){
			{this.setOpaque(true);}
			private static final long serialVersionUID = 1L;
			public String getText(){
				return ts == null ?super.getText():ts.getKey().toString();
			}
			public Color getBackground(){
				if(thermometer == null) return super.getBackground();
				return Color.decode(thermometer.get(StringKey.color));
			}
			public Color getForeground(){
				return Thermometer.getBW(getBackground());
			}
		};
		JCheckBox box = new JCheckBox("",true);
		private Thermometer thermometer;
		public JCheckBoxButton(TimeSeries ts)
		{
			super(new BorderLayout());
			this.thermometer = (Thermometer)ts.getKey();
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
					dlg.setContentPane(new EditorPanel(JCheckBoxButton.this));
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

		public EditorPanel(JCheckBoxButton button)
		{
			super(new BorderLayout());
			JPanel westWestPanel = new JPanel(new GridBagLayout());
			TimeSeries ts = button.ts;
			Thermometer thermometer = (Thermometer) ts.getKey();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx=gbc.gridy=0;
			gbc.weightx=gbc.weighty=1;
			for(Entry<Object, Object> entry : thermometer.entrySet())
			{
				if(entry.getKey() instanceof Key)
				{
					if(entry.getKey().toString().equals(IntKey.probeindex.toString())) continue;
					JComponent cmp = null;
					Key<?> key = (Key<?>) entry.getKey();
					Object value = entry.getValue();
					if(value instanceof Integer)
					{
						JPanel panel = new JPanel(new BorderLayout());
						cmp = panel;
						panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
						SpinnerNumberModel model = new SpinnerNumberModel((int)value,0, 2000, 1);
						panel.add(new JSpinner(model ),BorderLayout.CENTER);
					}
					else if(key.toString().equals(StringKey.color.toString()))
					{
						JButton colorButton = new JButton("line color");
						cmp = colorButton;
						colorButton.setBackground(Thermometer.getColor(value.toString()));
						colorButton.setForeground(Thermometer.getBW(colorButton.getBackground()));
						colorButton.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								Color color = JColorChooser.showDialog(null, "choose line color", colorButton.getBackground());
								if(color == null) return;
								thermometer.put(StringKey.color, Thermometer.getString(color));
								colorButton.setBackground(color);
								colorButton.setForeground(Thermometer.getBW(colorButton.getBackground()));
								int seriesIndex = dataset.getSeriesIndex(ts.getKey());
								chartPanel.getChart().getXYPlot().getRenderer().setSeriesPaint(seriesIndex, color);
								button.revalidate();
								button.repaint();
							}
						});
					}
					else if(key.toString().equals(StringKey.label.toString()))
					{
						JPanel panel = new JPanel(new BorderLayout());
						cmp = panel;
						panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
						JComboBox<String> box = new JComboBox<String>(new String[]{"probe "+thermometer.getIndex(),"ambient "+thermometer.getIndex(),"beef "+thermometer.getIndex(),"chicken "+thermometer.getIndex(),"pork "+thermometer.getIndex()});
						box.setEditable(true);
						box.setSelectedItem(value.toString());
						panel.add(box,BorderLayout.CENTER);
						box.setPreferredSize(new Dimension(100, 24));
						box.getEditor().getEditorComponent().addFocusListener(new FocusListener() {
							@Override
							public void focusLost(FocusEvent e)
							{
								thermometer.setLabel(box.getSelectedItem().toString());
								box.setSelectedItem(thermometer.get(StringKey.label));
								SwingUtilities.getWindowAncestor(boxPanel).validate();
								SwingUtilities.getWindowAncestor(boxPanel).repaint();
							}

							@Override
							public void focusGained(FocusEvent e) {
							}
						});
					}
					else if(value instanceof String)
					{
						JPanel panel = new JPanel(new BorderLayout());
						cmp = panel;
						panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
						JTextField field = new JTextField(key.toString());
						panel.add(field,BorderLayout.CENTER);
						field.setPreferredSize(new Dimension(100, 24));
						field.setText(value.toString());
					}
					if(cmp!=null)
					{
						westWestPanel.add(cmp,gbc);
						gbc.gridy++;
					}
				}
			}
			final JPanel westPanel = new JPanel();
			JPanel alertPanel = new JPanel();
			DefaultListModel<TemperatureAlert> model = new DefaultListModel<>();
			JList<TemperatureAlert> list = new JList<>(model);
			list.addListSelectionListener(new ListSelectionListener() {
				private JPanel panel = alertPanel;
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if(list.getSelectedValue()!=null){
						EditorPanel.this.remove(panel);
						EditorPanel.this.add(panel = list.getSelectedValue().getPanel(),BorderLayout.CENTER);
						EditorPanel.this.validate();
					}						
				}
			});
			westWestPanel.add(new JButton(new AbstractAction("comments"){
				private static final long serialVersionUID = -5989081975520668735L;
				@Override
				public void actionPerformed(ActionEvent e) {
					comment(thermometer.timeCommentMap);
				}}),gbc);
			gbc.gridy++;
			westWestPanel.add(new JButton(new AbstractAction("add alert..."){
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					TemperatureAlert alert = new TemperatureAlert();
					thermometer.alertSet.add(alert);
					model.clear();
					for(TemperatureAlert element : thermometer.alertSet)
						model.addElement(element);
				}}),gbc);
			gbc.gridy++;
			westWestPanel.add(new JButton(new AbstractAction("delete alert..."){
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					if(list.getSelectedIndex()<0)return;
					thermometer.alertSet.remove(model.getElementAt(list.getSelectedIndex()));
					model.clear();
					for(TemperatureAlert alert : thermometer.alertSet)
						model.addElement(alert);
				}}),gbc);
			for(TemperatureAlert alert : thermometer.alertSet)
				model.addElement(alert);
			westPanel.add(westWestPanel,BorderLayout.WEST);
			westPanel.add(new JScrollPane(list),BorderLayout.CENTER);
			add(westPanel,BorderLayout.WEST);
			add(alertPanel,BorderLayout.CENTER);
		}
	}
	public class HintTextArea extends JTextArea {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public HintTextArea(String hint) {
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
	public static void comment(LinkedHashMap<Long,String> commentMap)
	{
		int height = 300;
		int areaWidth = 300;
		JTextArea area = new JTextArea();
		area.setPreferredSize(new Dimension(areaWidth,height));
		DefaultListModel<Date> model = new DefaultListModel<>();
		fill(model,commentMap);
		JList<Date> list = new JList<>(model);
		int listWidth = 150;
		list.setPreferredSize(new Dimension(listWidth,height));
		area.setEnabled(false);
		area.addFocusListener(new FocusAdapter() {
			int indexToSet = -1;
			@Override
			public void focusGained(FocusEvent e) {
				indexToSet = list.getSelectedIndex();
			}
			@Override
			public void focusLost(FocusEvent e) {
				int index=0;
				long key = -1;
				for(Entry<Long, String> entry : commentMap.entrySet())
					if(index++ == indexToSet)
					{
						key = entry.getKey();
						break;
					}
				index--;
				commentMap.put(key, area.getText());
			}			
		});
		
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) return;
				if(list.getSelectedIndex()<0)
				{
					area.setEnabled(false);
					return;
				}
				area.setEnabled(true);
				int index=0;
				for(Entry<Long, String> entry : commentMap.entrySet())
					if(index++ == list.getSelectedIndex())
					{
						area.setText(entry.getValue());
						return;
					}
				list.setSelectedIndex(model.getSize()-1);
			}
		});
		JButton add = new JButton(new AbstractAction("add"){
			private static final long serialVersionUID = -6488437817173769878L;
			@Override
			public void actionPerformed(ActionEvent e) {
				String comment = "comment:";
				commentMap.put(System.currentTimeMillis(), comment);
				fill(model,commentMap);
				list.setSelectedIndex(model.size()-1);
			}});
		JButton delete = new JButton(new AbstractAction("delete"){
			private static final long serialVersionUID = -6488437817173769878L;
			@Override
			public void actionPerformed(ActionEvent e) {
				int index=0;
				long key = -1;
				for(Entry<Long, String> entry : commentMap.entrySet())
					if(index++ == list.getSelectedIndex())
					{
						key = entry.getKey();
						break;
					}
				commentMap.remove(key);
				fill(model,commentMap);
				if(model.getSize()<=0)
					area.setText("");
				else
					list.setSelectedIndex(model.size()-1);
			}});
		
		JPanel panel =new JPanel(new GridBagLayout());
		panel.setPreferredSize(new Dimension(listWidth+10, height));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=gbc.gridy=0;
		gbc.weightx=1;
		gbc.weighty=.9;
		gbc.gridwidth=2;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JScrollPane(list),gbc);
		gbc.weighty=.1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth=1;
		gbc.gridy++;
		panel.add(add,gbc);
		gbc.gridx++;
		panel.add(delete,gbc);
		JPanel commentPanel = new JPanel(new BorderLayout());
		commentPanel.add(panel,BorderLayout.WEST);
		commentPanel.add(area,BorderLayout.CENTER);
		JDialog dlg = new JDialog();
		dlg.setTitle("Comment Editor");
		dlg.setContentPane(commentPanel);
		dlg.setSize((int) (area.getPreferredSize().getWidth()+panel.getPreferredSize().getWidth()),height);
		dlg.pack();
		dlg.setVisible(true);
	}
	private static void fill(DefaultListModel<Date> model, LinkedHashMap<Long, String> commentMap) {
		model.clear();
		for(Entry<Long, String> entry : commentMap.entrySet())
			model.addElement(new Date(entry.getKey()){
				private static final long serialVersionUID = 5676390436652624926L;
				public String toString(){
					return super.toString().substring(11,19);
				}
			});
	}
}


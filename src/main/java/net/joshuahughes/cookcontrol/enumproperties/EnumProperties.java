package net.joshuahughes.cookcontrol.enumproperties;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.AbstractMap;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("unchecked")
public class EnumProperties extends Properties
{
	private static final long serialVersionUID = -3548459685215964550L;
	public interface Key<T>{}
	public static enum LongKey implements Key<Long>{utctime,sleep}
	public static enum IntKey implements Key<Integer>{fantemperatureindex,probeindex}
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature}
	public static enum StringKey implements Key<String>{label, email, color}
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound}
	public <T> T get(Key<T> key){return (T) super.get(key);}
	public <T> T put(Key<T> key,T value){return (T) super.put(key,value);}
	public LinkedHashMap<Long,String> timeCommentMap = new LinkedHashMap<>();
	protected JPanel westPanel = new JPanel();
	private JPanel panel = new JPanel(new BorderLayout());
	protected String[] labelOptions = new String[]{};
	public Object get( Object key)
	{
		Key<?> enumKey = getKeyValue(key.toString(),null).getKey();
		return super.get(enumKey==null?key:enumKey);
	}
	private static <T> Entry<Key<T>,T> getKeyValue(String stringKey,String stringValue) {
		Key<T> key = null;
		T value = null;
		try{key = (Key<T>) LongKey.valueOf(stringKey);value = (T)new Long(Long.parseLong(stringValue));}catch(Exception e){}
		try{key = (Key<T>) FloatKey.valueOf(stringKey);value = (T)new Float(Float.parseFloat(stringValue));}catch(Exception e){}
		try{key = (Key<T>) IntKey.valueOf(stringKey);value = (T)new Integer(Integer.parseInt(stringValue));}catch(Exception e){}
		return new AbstractMap.SimpleEntry<Key<T>,T>(key,value);
	}
	protected void init()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=gbc.gridy=0;
		gbc.weightx=gbc.weighty=1;
		westPanel.setLayout(new GridBagLayout());
		for(Entry<Object, Object> entry : entrySet())
		{
			if(entry.getKey() instanceof Key)
			{
				JComponent cmp = getComponent(this, (Key<?>) entry.getKey(), entry.getValue());
				if(cmp!=null)
				{
					westPanel.add(cmp,gbc);
					gbc.gridy++;
				}
			}
		}
		
		JTextArea area = new JTextArea();
		DefaultListModel<Date> model = new DefaultListModel<>();
		fill(model,timeCommentMap);
		JList<Date> list = new JList<>(model);
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
				for(Entry<Long, String> entry : timeCommentMap.entrySet())
					if(index++ == indexToSet)
					{
						key = entry.getKey();
						break;
					}
				index--;
				timeCommentMap.put(key, area.getText());
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
				for(Entry<Long, String> entry : timeCommentMap.entrySet())
					if(index++ == list.getSelectedIndex())
					{
						area.setText(entry.getValue());
						return;
					}
				list.setSelectedIndex(model.getSize()-1);
			}
		});
		
		westPanel.add(new JButton(new AbstractAction("add comment"){
			private static final long serialVersionUID = -6488437817173769878L;
			@Override
			public void actionPerformed(ActionEvent e) {
				String comment = "comment:";
				timeCommentMap.put(System.currentTimeMillis(), comment);
				fill(model,timeCommentMap);
				list.setSelectedIndex(model.size()-1);
			}}),gbc);
		
		gbc.gridy++;
		westPanel.add(new JButton(new AbstractAction("delete comment"){
			private static final long serialVersionUID = -6488437817173769878L;
			@Override
			public void actionPerformed(ActionEvent e) {
				int index=0;
				long key = -1;
				for(Entry<Long, String> entry : timeCommentMap.entrySet())
					if(index++ == list.getSelectedIndex())
					{
						key = entry.getKey();
						break;
					}
				timeCommentMap.remove(key);
				fill(model,timeCommentMap);
				if(model.getSize()<=0)
					area.setText("");
				else
					list.setSelectedIndex(model.size()-1);
			}}),gbc);
		
		gbc.gridy++;
		westPanel.add(new JScrollPane(list), gbc);

		gbc.gridy++;
		gbc.fill = GridBagConstraints.BOTH;
		westPanel.add(new JScrollPane(area), gbc);

		panel.add(westPanel, BorderLayout.WEST);
	}
	public JPanel getPanel()
	{
		return panel;
	}
	public JComponent getComponent(EnumProperties props,Key<?> key,Object value)
	{
		JComponent cmp = null;
		if(key.toString().equals(StringKey.color.toString()))
		{
			JButton colorButton = new JButton("line color");
			cmp = colorButton;
			colorButton.setBackground(Thermometer.getColor(value.toString()));
			colorButton.setForeground(Thermometer.getBW(colorButton.getBackground()));
			colorButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Color color = JColorChooser.showDialog(null, "chart color", colorButton.getBackground());
					if(color == null) return;
					props.put(StringKey.color, EnumProperties.getString(color));
					colorButton.setBackground(color);
					colorButton.setForeground(Thermometer.getBW(colorButton.getBackground()));
				}
			});
		}
		else if(key.toString().equals(StringKey.label.toString()))
		{
			JPanel panel = new JPanel(new BorderLayout());
			cmp = panel;
			panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
			JComboBox<String> box = new JComboBox<String>(labelOptions);
			box.setEditable(true);
			box.setSelectedItem(value.toString());
			panel.add(box,BorderLayout.CENTER);
			box.setPreferredSize(new Dimension(100, 24));
			box.getEditor().getEditorComponent().addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e)
				{
					props.put(StringKey.label, box.getSelectedItem().toString());
					box.setSelectedItem(props.get(StringKey.label));
				}

				@Override
				public void focusGained(FocusEvent e) {
				}
			});
			box.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					EnumProperties.this.put(key, box.getSelectedItem());
				}
			});
		}
		else if(key instanceof LongKey)
		{
			JPanel panel = new JPanel(new BorderLayout());
			cmp = panel;
			panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
			SpinnerNumberModel model = new SpinnerNumberModel((int)value,0, 2000, 1);
			panel.add(new JSpinner(model ),BorderLayout.CENTER);
			model.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					put(key,value);
				}
			});
		}
		else if(key instanceof FloatKey)
		{
			JPanel panel = new JPanel(new BorderLayout());
			cmp = panel;
			panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
			SpinnerNumberModel model = new SpinnerNumberModel((int)value,0, 2000, 1);
			panel.add(new JSpinner(model ),BorderLayout.CENTER);
			model.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					put(key,value);
				}
			});
		}
		else if(key instanceof IntKey)
		{
			JPanel panel = new JPanel(new BorderLayout());
			cmp = panel;
			panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
			SpinnerNumberModel model = new SpinnerNumberModel((int)value,0, 2000, 1);
			panel.add(new JSpinner(model),BorderLayout.CENTER);
			model.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					put(key,value);
				}
			});
		}

		else if(key instanceof StringKey)
		{
			JPanel panel = new JPanel(new BorderLayout());
			cmp = panel;
			panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
			JTextField field = new JTextField(key.toString());
			panel.add(field,BorderLayout.CENTER);
			field.setPreferredSize(new Dimension(100, 24));
			field.setText(value.toString());
			field.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					put(key,value);
				}
			});
		}
		else if(key instanceof BooleanKey)
		{
			JCheckBox box = new JCheckBox(key.toString(),(boolean) value);
			cmp = box;
			box.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					put(key,box.isSelected());
				}
			});
		}
		return cmp;
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
	public static String getString(Color color)
	{
		return "0x"+Integer.toHexString(color.getRGB()).substring(2);
	}
	public static Color getColor(String string)
	{
		return Color.decode(string);
	}
	public static Color getBW(Color color)
	{
		  double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
		  return y >= 128 ? Color.black : Color.white;
	}

}

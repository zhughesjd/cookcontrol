package net.joshuahughes.smokercontroller.enumproperties;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	protected JPanel panel = new JPanel();
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
	public JPanel getPanel()
	{
		return panel;
	}
	public JComponent getComponent(EnumProperties props,Key<?> key,Object value)
	{
		JComponent cmp = null;
		if(key.toString().equals(StringKey.label.toString()))
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

}

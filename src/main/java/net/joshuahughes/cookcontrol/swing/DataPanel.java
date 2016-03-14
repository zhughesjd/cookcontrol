package net.joshuahughes.cookcontrol.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.joshuahughes.cookcontrol.Key;
import net.joshuahughes.cookcontrol.data.Alert;
import net.joshuahughes.cookcontrol.data.Data;
import net.joshuahughes.cookcontrol.data.Thermometer;
import net.joshuahughes.cookcontrol.data.property.Property;
import net.joshuahughes.cookcontrol.data.property.StringProperty.StringKey;


@SuppressWarnings("unchecked")
public abstract class DataPanel<C extends Data<?>,D extends Data<C>,CP extends DataPanel<?,?,?>> extends JPanel
{
	private static final long serialVersionUID = -4605336947758325544L;
	
	public static String parametersFileName = DataPanel.class.getSimpleName().toLowerCase()+".txt";

	protected ArrayList<CP> children = new ArrayList<CP>();
	private LinkedHashMap<Object,Object> map = new LinkedHashMap<>();
	private Vector<String> candidateLabels = new Vector<String>();
	private LinkedHashSet<MouseAdapter> set = new LinkedHashSet<>();
	ChildPanel childPanel;
	public DataPanel(D data,String... candidateLabelsArray) throws Exception
	{
		super(new GridBagLayout());
		init(data);
		childPanel = new ChildPanel(children);
		CommentPanel commentPanel = new CommentPanel(data.getComment());
		candidateLabels.addAll(Arrays.asList(candidateLabelsArray));
		String value = data.getValue(StringKey.label);
		if(value!=null) candidateLabels.add(value);
			
		for(Property<?,?> property : data.getAllProperties())
		{
			putObject(property.getKey(),property.getValue());
		}
		JPanel centerPanel = new JPanel(new GridBagLayout());
		centerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),"Parameters"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1;
		for(Entry<Object, Object> entry : map.entrySet())
			if(entry.getValue() instanceof Component)
			{
				centerPanel.add((Component) entry.getValue(), gbc);
				gbc.gridy++;
			}

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1;
		add(centerPanel,gbc);
		gbc.gridx++;
		add(commentPanel,gbc);
		gbc.gridx++;
		add(childPanel,gbc);
	}
	private <V,K extends Key<V>> V putObject(K key,Object value)
	{
		return put(key,(V)value);
	}
	public void load(File directory)
	{
	}
	protected abstract void init(D type) throws Exception;
	public <V,K extends Key<V>> V get(K key)
	{
		return process(key);
	}
	public <V,K extends Key<V>> V put(K key,V value)
	{
		Class<?> clazz = (Class<?>) ((ParameterizedType)key.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
		return clazz.isInstance(value)?(V) map.put(key,value):putComponent(key,value);
	}
	public <V,K extends Key<V>> V putComponent(K key,V... value)
	{
		return process(key,value);
	}
	private LinkedHashSet<FocusListener> focusListenerSet = new LinkedHashSet<>();
	public boolean addLabelListener(FocusListener listener)
	{
		return focusListenerSet.add(listener);
	}
	private <V,K extends Key<V>> V process(K key,V... array)
	{
		Object returnValue = map.get(key);
		//"get call" with no value for key
		if(returnValue == null && (array == null || array.length==0)) return null;

		Class<?> clazz = (Class<?>) ((ParameterizedType)key.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
		if(clazz.isInstance(returnValue)) return (V) returnValue;
		if(key.equals(StringKey.label))
		{
			LabeledComponent<JComboBox<String>> box = (LabeledComponent<JComboBox<String>>) returnValue;
			if(box == null)
			{
				JPanel panel = new JPanel(new BorderLayout());
				panel.add(new JLabel(key.toString()+": "),BorderLayout.WEST);
				box = new LabeledComponent<>(key,new JComboBox<String>(candidateLabels));
				box.getComponent().setEditable(true);
				box.getComponent().setSelectedItem(array[0].toString());
				box.getComponent().addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent event)
					{
						for(FocusListener l : focusListenerSet)
							l.focusLost(event);
					}
				});
				panel.add(box,BorderLayout.CENTER);
				box.setPreferredSize(new Dimension(150, 24));
				map.put(key,box);
				return null;
			}
			else
			{
				V value = (V) box.getComponent().getSelectedItem().toString();
				// get
				if(array.length<=0) return value;
				//put
				box.getComponent().setSelectedItem(array[0].toString());
				return value;
			}
		}
		else if(key.equals(StringKey.color))
		{
			JButton button = (JButton) returnValue;
			String colorString = array[0].toString();
			if(button == null)
			{
				button = new JButton(StringKey.color.name());
				button.setBackground(DataPanel.getColor(colorString));
				button.setForeground(DataPanel.getBW(button.getBackground()));
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JButton thisButton = (JButton) e.getSource();
						Color color = JColorChooser.showDialog(null, "choose color", thisButton.getBackground());
						if(color == null) return;
						thisButton.setBackground(color);
						thisButton.setForeground(Key.getBW(thisButton.getBackground()));
					}
				});
				map.put(key,button);
				return(V)null;
			}
			else
			{
				V value = (V) button.getBackground();
				// get
				if(array.length<=0) return value;
				//put
				button.setBackground(DataPanel.getColor(array[0].toString()));
				button.setForeground(DataPanel.getBW(button.getBackground()));
				return value;
			}
		}
		else if(Number.class.isAssignableFrom(clazz))
		{
			LabeledComponent<JSpinner> spinner = (LabeledComponent<JSpinner>) returnValue;
			if(spinner == null)
			{
				Number[] params = new Number[]{1,0,10000,1};
				for(int index=0;index<Math.min(params.length,array.length);index++)
					params[index] =(Number) array[index];
				SpinnerNumberModel model = 
						clazz.equals(Float.class) || clazz.equals(Double.class) ?
								new SpinnerNumberModel(params[0].doubleValue(), params[1].doubleValue(), params[2].doubleValue(), params[3].doubleValue()) :
									new SpinnerNumberModel(params[0].intValue(), params[1].intValue(), params[2].intValue(), params[3].intValue());
								map.put(key, new LabeledComponent<JSpinner>(key,new JSpinner(model)));
								return (V)null;
			}
			else
			{
				V value = (V) spinner.getComponent().getValue();
				// get
				if(array.length<=0) return value;
				//put
				spinner.getComponent().setValue(array[0]);
				return value;
			}
		}
		else if(Boolean.class.isAssignableFrom(clazz))
		{
			JCheckBox box = (JCheckBox) returnValue;
			if(box == null)
			{
				Boolean flag = array.length>=1?(boolean) array[0]:true;
				map.put(key,new JCheckBox(key.toString(),flag));
				return null;
			}
			else
			{
				V value = (V)(Boolean)box.isSelected();
				// get
				if(array.length<=0) return value;
				//put
				box.setSelected((boolean) array[0]);
				return value;
			}
		}
		else if(String.class.isAssignableFrom(clazz))
		{
			LabeledComponent<JTextField> field = (LabeledComponent<JTextField>) returnValue;
			if(field == null)
			{
				map.put(key,new LabeledComponent<JTextField>(key, new JTextField(array[0].toString())));
				return null;
			}
			else
			{
				V value = (V) field.getComponent().getText();
				// get
				if(array.length<=0) return value;
				//put
				field.getComponent().setText(array[0].toString());
				return value;
			}
		}
		return (V)returnValue;
	}
	public String toString()
	{
		String string = get(StringKey.label);
		return string == null?"":string;
	}
	public static class LabeledComponent<C extends Component> extends JPanel
	{
		private static final long serialVersionUID = 96159677943410449L;
		private C component;
		public LabeledComponent(Key<?> key,C component)
		{
			super(new BorderLayout());
			add(new JLabel(key.toString()+": "),BorderLayout.WEST);
			add(this.component = component,BorderLayout.CENTER);
			component.setPreferredSize(new Dimension(300, 24));
		}
		public C getComponent() {
			return component;
		}
	}
	public void addMouseListener(MouseAdapter adapter)
	{
		set.add(adapter);
		childPanel.getChildList().addMouseListener(adapter);
		for(CP child : this.children)
			child.addMouseListener(adapter);
	}
	public class ChildPanel extends JPanel
	{
		private static final long serialVersionUID = -3623155910463151412L;
		private BackedListModel<CP> children;
		private JList<CP> list;
		public ChildPanel(List<CP> c)
		{
			super(new GridBagLayout());
			JScrollPane pane = new JScrollPane(list = new JList<>(children = new BackedListModel<>(c)));
			Class<?> clazz = (Class<?>) ((ParameterizedType)DataPanel.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),clazz.getSimpleName()));
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.weightx = gbc.weighty = 1;
			gbc.gridx = gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.VERTICAL;
			add(pane,gbc);
			if(DataPanel.this.getClass().equals(Thermometer.class))
			{
				gbc.gridy++;
				gbc.gridwidth=1;
				add(new JButton(new AbstractAction("add") {
					private static final long serialVersionUID = 1L;
					@Override
					public void actionPerformed(ActionEvent e) {
						try{
							children.addElement((CP) new AlertPanel(new Alert()));
							ChildPanel.this.validate();
							ChildPanel.this.repaint();
						}catch(Exception exception)
						{
							exception.printStackTrace();
						}
					}
				}),gbc);
				gbc.gridx++;
				add(new JButton(new AbstractAction("remove") {
					private static final long serialVersionUID = 1L;
					@Override
					public void actionPerformed(ActionEvent e) {
						children.removeElement(list.getSelectedValue());
						ChildPanel.this.validate();
						ChildPanel.this.repaint();
					}
				}),gbc);

			}
		}
		public JList<CP> getChildList()
		{
			return list;
		}
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
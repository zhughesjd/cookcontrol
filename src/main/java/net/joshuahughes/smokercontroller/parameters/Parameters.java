package net.joshuahughes.smokercontroller.parameters;

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
import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Collections;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.joshuahughes.smokercontroller.enumproperties.Thermometer;
import net.joshuahughes.smokercontroller.xml.Alerttype;
import net.joshuahughes.smokercontroller.xml.Platformtype;
import net.joshuahughes.smokercontroller.xml.Smokercontrollertype;
import net.joshuahughes.smokercontroller.xml.Smoketype;
import net.joshuahughes.smokercontroller.xml.Thermometertype;
import net.joshuahughes.smokercontroller.xml.Type;
import net.joshuahughes.smokercontroller.xml.Type.Property;

@SuppressWarnings("unchecked")
public abstract class Parameters<T extends Type,C> extends JPanel{
	private static final long serialVersionUID = -4605336947758325544L;
	public interface Key<T>{public T fromString(String s);}
	public static enum LongKey implements Key<Long>{utctime,sleep;public Long fromString(String s){return Long.valueOf(s);}}
	public static enum IntKey implements Key<Integer>{fantemperatureindex,index;public Integer fromString(String s){return Integer.valueOf(s);}}
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature;public Float fromString(String s){return Float.valueOf(s);}}
	public static enum StringKey implements Key<String>{label, email, color,macaddress;public String fromString(String s){return String.valueOf(s);}}
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound;public Boolean fromString(String s){return Boolean.valueOf(s);}}
	public static int idIncr = 0;
	public static String parametersFileName = Parameters.class.getSimpleName().toLowerCase()+".txt";

	private LinkedHashMap<Object,Object> map = new LinkedHashMap<>();
	protected ChildPanel childPanel;
	private Vector<String> candidateLabels = new Vector<String>();
	protected T type;
	private CommentPanel commentPanel;
	public Parameters(T type,String... candidateLabelsArray) throws Exception
	{
		super(new GridBagLayout());
		this.type = type;
		init();
		childPanel = new ChildPanel(getChildren(type));
		commentPanel = new CommentPanel(type.getComment());
		putComponent(StringKey.label,this.getClass().getSimpleName().toLowerCase()+" "+idIncr++);
		candidateLabels.addElement(this.getClass().getSimpleName().toLowerCase());
		candidateLabels.addAll(Arrays.asList(candidateLabelsArray));

		for(Property property : type.getProperty())
		{
			String[] keyParts = property.getKey().toString().split("\\.");
			Key<?> key = null;
			if(LongKey.class.getSimpleName().equals(keyParts[0]))
				key = Enum.valueOf(LongKey.class,property.getValue().toString());
			if(IntKey.class.getSimpleName().equals(keyParts[0]))
				key = Enum.valueOf(IntKey.class,property.getValue().toString());
			if(FloatKey.class.getSimpleName().equals(keyParts[0]))
				key = Enum.valueOf(FloatKey.class,property.getValue().toString());
			if(StringKey.class.getSimpleName().equals(keyParts[0]))
				key = Enum.valueOf(StringKey.class,property.getValue().toString());
			if(BooleanKey.class.getSimpleName().equals(keyParts[0]))
				key = Enum.valueOf(BooleanKey.class,property.getValue().toString());
			if(key!=null)
				this.putObject(key,key.fromString(property.getValue().toString()));
		}
		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1;
		for(Entry<Object, Object> entry : map.entrySet())
			if(entry.getValue() instanceof Component)
			{
				centerPanel.add((Component) entry.getValue(), gbc);
				gbc.gridy++;
			}
		centerPanel.add(new JButton(new AbstractAction("comments") {
			private static final long serialVersionUID = 2696590018143251384L;
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dlg = new JDialog();
				dlg.setTitle("Comment Editor");
				dlg.setContentPane(commentPanel);
				dlg.pack();
				dlg.setSize(500,500);
				dlg.setVisible(true);
			}
		}),gbc);
		gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.weightx = gbc.weighty = 1;
		add(centerPanel,gbc);
		gbc.gridx++;
		add(childPanel,gbc);
	}
	private List<C> getChildren(T type) {
		List<C> list = Collections.emptyList();
		if(type.getClass().equals(Thermometertype.class))
			list = (List<C>) ((Thermometertype)type).getAlert();
		if(type.getClass().equals(Smoketype.class))
			list = (List<C>) ((Smoketype)type).getThermometer();
		if(type.getClass().equals(Platformtype.class))
			list = (List<C>) ((Platformtype)type).getSmoke();
		if(type.getClass().equals(Smokercontrollertype.class))
			list = (List<C>) ((Smokercontrollertype)type).getPlatform();
		return list ;
	}
	private <V,K extends Key<V>> V putObject(K key,Object value)
	{
		return put(key,(V)value);
	}
	public void load(File directory)
	{
	}
	public abstract void init();
	public ChildPanel getChildPanel()
	{
		return childPanel;
	}
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
				button.setBackground(Parameters.getColor(colorString));
				button.setForeground(Parameters.getBW(button.getBackground()));
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JButton thisButton = (JButton) e.getSource();
						Color color = JColorChooser.showDialog(null, "choose color", thisButton.getBackground());
						if(color == null) return;
						thisButton.setBackground(color);
						thisButton.setForeground(Thermometer.getBW(thisButton.getBackground()));
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
				button.setBackground(Parameters.getColor(array[0].toString()));
				button.setForeground(Parameters.getBW(button.getBackground()));
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
	public class ChildPanel extends JPanel
	{
		private static final long serialVersionUID = -3623155910463151412L;
		private BackedListModel<C> children;
		private JList<C> list;
		public ChildPanel(List<C> c)
		{
			super(new GridBagLayout());
			JScrollPane pane = new JScrollPane(list = new JList<>(children = new BackedListModel<>(c)));
			Class<?> clazz = (Class<?>) ((ParameterizedType)Parameters.this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),clazz.getSimpleName()));
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.weightx = gbc.weighty = 1;
			gbc.gridx = gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.fill = GridBagConstraints.VERTICAL;
			add(pane,gbc);

			if(Parameters.this.getClass().equals(Thermometer.class))
			{
				gbc.gridy++;
				gbc.gridwidth=1;
				add(new JButton(new AbstractAction("add") {
					private static final long serialVersionUID = 1L;
					@Override
					public void actionPerformed(ActionEvent e) {
						try{
							children.addElement((C) new Alert(new Alerttype()));
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
		public JList<C> getChildList()
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
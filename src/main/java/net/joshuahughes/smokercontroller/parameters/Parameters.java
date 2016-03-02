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
import java.lang.reflect.ParameterizedType;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.joshuahughes.smokercontroller.enumproperties.Thermometer;

@SuppressWarnings("unchecked")
public abstract class Parameters<C> extends JPanel{
	private static final long serialVersionUID = -4605336947758325544L;
	public interface Key<T>{}
	public static enum LongKey implements Key<Long>{utctime,sleep}
	public static enum IntKey implements Key<Integer>{fantemperatureindex,index}
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature}
	public static enum StringKey implements Key<String>{label, email, color,macaddress}
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound}
	public int idIncr = 0;
	private LinkedHashMap<Object,Object> map = new LinkedHashMap<>();
	private LinkedHashMap<Long,String> comments = new LinkedHashMap<>();
	protected ChildPanel childPanel = new ChildPanel();
	private Vector<String> candidateLabels = new Vector<String>();
	public Parameters(String... candidateLabelsArray)
	{
		super(new BorderLayout());
		putComponent(StringKey.label,this.getClass().getSimpleName().toLowerCase()+" "+idIncr++);
		candidateLabels.addElement(this.getClass().getSimpleName().toLowerCase());
		candidateLabels.addAll(Arrays.asList(candidateLabelsArray));
	}
	public void initialize()
	{
		JPanel centerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = gbc.weighty = 1;
		gbc.gridx = gbc.gridy = 0;
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
				Parameters.comment(comments);
			}
		}),gbc);
		add(centerPanel,BorderLayout.CENTER);
		add(childPanel,BorderLayout.EAST);
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
				panel.add(box,BorderLayout.CENTER);
				box.setPreferredSize(new Dimension(100, 24));
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
	public abstract boolean addChildOperations();
	public abstract C createChild();
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
		private DefaultListModel<C> children = new DefaultListModel<>();
		private JList<C> list = new JList<>(children);
		public ChildPanel()
		{
			super(new GridBagLayout());
			JScrollPane pane = new JScrollPane(list);
			Class<?> clazz = (Class<?>) ((ParameterizedType)Parameters.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),clazz.getSimpleName()));
			GridBagConstraints gbc = new GridBagConstraints();

			gbc.weightx = gbc.weighty = 1;
			gbc.gridx = gbc.gridy = 0;
			gbc.gridwidth = 2;
			add(pane,gbc);
			
			if(addChildOperations())
			{
				gbc.gridy++;
				gbc.gridwidth=1;
				add(new JButton(new AbstractAction("add") {
					private static final long serialVersionUID = 1L;
					@Override
					public void actionPerformed(ActionEvent e) {
						children.addElement(createChild());
						ChildPanel.this.validate();
						ChildPanel.this.repaint();
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
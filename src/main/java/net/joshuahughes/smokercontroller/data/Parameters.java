package net.joshuahughes.smokercontroller.data;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

@SuppressWarnings("unchecked")
public abstract class Parameters<C> extends JPanel{
	private static final long serialVersionUID = -4605336947758325544L;
	public interface Key<T>{}
	public static enum LongKey implements Key<Long>{utctime,sleep}
	public static enum IntKey implements Key<Integer>{fantemperatureindex,index}
	public static enum FloatKey implements Key<Float>{sensortemperature,mintemperature,fanrpm,maxtemperature}
	public static enum StringKey implements Key<String>{label, email, color}
	public static enum BooleanKey implements Key<Boolean>{light,vibrate,sound}
	private LinkedHashMap<Object,Object> map = new LinkedHashMap<>();
	private LinkedHashMap<Long,String> comments = new LinkedHashMap<>();
	protected ChildPanel childPanel = new ChildPanel();
	public Parameters()
	{
		super(new BorderLayout());
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
				System.out.println(comments.size());
			}
		}),gbc);
		add(centerPanel,BorderLayout.CENTER);
		add(childPanel,BorderLayout.EAST);
	}
	public <V,K extends Key<V>> V get(K key)
	{
		return process(key);
	}
	public <V,K extends Key<V>> V put(K key,V... value)
	{
		return process(key,value);
	}
	private <V,K extends Key<V>> V process(K key,V... array)
	{
		Object returnValue = map.get(key);

		//"get call" with no value for key
		if(returnValue == null && (array == null || array.length==0)) return null;
		
		Class<?> clazz = (Class<?>) ((ParameterizedType)key.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
		if(Number.class.isAssignableFrom(clazz))
		{
			LabeledComponent<JSpinner> spinner = (LabeledComponent<JSpinner>) map.get(key);
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
		if(Boolean.class.isAssignableFrom(clazz))
		{
			JCheckBox box = (JCheckBox) map.get(key);
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
		if(String.class.isAssignableFrom(clazz))
		{
			LabeledComponent<JTextField> field = (LabeledComponent<JTextField>) map.get(key);
			if(field == null)
			{
				map.put(key,new LabeledComponent<JTextField>(key, new JTextField(key.toString()+": ")));
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
			component.setPreferredSize(new Dimension(100, 24));
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
			Class<?> clazz = Parameters.getClass(Parameters.this.getClass());
			JScrollPane pane = new JScrollPane(list);
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
					}
				}),gbc);
				gbc.gridx++;
				add(new JButton(new AbstractAction("remove") {
					private static final long serialVersionUID = 1L;
					@Override
					public void actionPerformed(ActionEvent e) {
						children.removeElement(list.getSelectedValue());
					}
				}),gbc);
				
			}
		}
	}
    public static Class<?> getClass(Type type)
    {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = getClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
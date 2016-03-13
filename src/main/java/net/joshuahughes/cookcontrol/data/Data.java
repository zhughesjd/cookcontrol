package net.joshuahughes.cookcontrol.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Random;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import ca.odell.glazedlists.BasicEventList;
import net.joshuahughes.cookcontrol.data.property.BooleanProperty;
import net.joshuahughes.cookcontrol.data.property.DateProperty;
import net.joshuahughes.cookcontrol.data.property.FloatProperty;
import net.joshuahughes.cookcontrol.data.property.IntegerProperty;
import net.joshuahughes.cookcontrol.data.property.IntegerProperty.IntegerKey;
import net.joshuahughes.cookcontrol.data.property.LongProperty;
import net.joshuahughes.cookcontrol.data.property.Property;
import net.joshuahughes.cookcontrol.data.property.StringProperty;
import net.joshuahughes.cookcontrol.data.property.StringProperty.StringKey;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Data <C extends Data<?>>{
	ArrayList<Comment> comment = new ArrayList<>();
	LinkedHashSet<StringProperty> stringproperty = new LinkedHashSet<>();
	LinkedHashSet<LongProperty> longproperty = new LinkedHashSet<>();
	LinkedHashSet<IntegerProperty> integerproperty = new LinkedHashSet<>();
	LinkedHashSet<BooleanProperty> booleanproperty = new LinkedHashSet<>();
	LinkedHashSet<DateProperty> dateproperty = new LinkedHashSet<>();
	LinkedHashSet<FloatProperty> floatproperty = new LinkedHashSet<>();
	
	public LinkedHashSet<StringProperty> getStringproperty() {
		return stringproperty;
	}
	public LinkedHashSet<LongProperty> getLongproperty() {
		return longproperty;
	}
	public LinkedHashSet<IntegerProperty> getIntegerproperty() {
		return integerproperty;
	}
	public LinkedHashSet<BooleanProperty> getBooleanproperty() {
		return booleanproperty;
	}
	public LinkedHashSet<DateProperty> getDateproperty() {
		return dateproperty;
	}
	public LinkedHashSet<FloatProperty> getFloatproperty() {
		return floatproperty;
	}
	@SuppressWarnings("unchecked")
	public LinkedHashSet<Property<?>> getProperty()
	{
		LinkedHashSet<Property<?>> allProperties = new LinkedHashSet<>();
		for(Object object : new Object[]{stringproperty,longproperty,integerproperty,booleanproperty,dateproperty,floatproperty})
			allProperties.addAll((Collection<? extends Property<?>>) object);
		return allProperties;
	}
	public ArrayList<Comment> getComment()
	{
		return comment;
	}
	public abstract BasicEventList<C> getChildren();
	public static void main(String[] args)
	{
		generateRandomXml();
	}
	public static void generateRandomXml()
	{
		Random random = new Random(System.currentTimeMillis());
		Cook cook = new Cook();
		for(int t=0;t<random.nextInt(11);t++)
		{
			Thermometer thermometer = new Thermometer(t);
			for(int a=0;a<random.nextInt(10);a++)
			{
				Alert alert = new Alert();
				thermometer.getChildren().add(alert);
				for(int c =0;c<random.nextInt(10);c++)
				{
					Comment comment = new Comment();
					comment.setRemark("remark:"+c);
					alert.getComment().add(comment);
				}
				StringProperty sp = new StringProperty();
				sp.setKey(StringKey.email);
				sp.setValue("sp");
				alert.getStringproperty().add(sp);

				StringProperty sp1 = new StringProperty();
				sp1.setKey(StringKey.email);
				sp1.setValue("sp1");
				alert.getStringproperty().add(sp1);

				IntegerProperty si = new IntegerProperty();
				si.setKey(IntegerKey.index);
				si.setValue(-2);
				alert.getIntegerproperty().add(si);

				thermometer.getChildren().add(alert);
			}
			for(int c =0;c<random.nextInt(10);c++)
			{
				Comment comment = new Comment();
				comment.setRemark("remark T:"+c);
				thermometer.getComment().add(comment);
			}
			StringProperty sp = new StringProperty();
			sp.setKey(StringKey.email);
			sp.setValue("therm");
			thermometer.getStringproperty().add(sp);

			IntegerProperty si = new IntegerProperty();
			si.setKey(IntegerKey.index);
			si.setValue(2);
			thermometer.getIntegerproperty().add(si);
			cook.getChildren().add(thermometer);
		}
		for(int c =0;c<random.nextInt(10);c++)
		{
			Comment comment = new Comment();
			comment.setRemark("remark C:"+c);
			cook.getComment().add(comment);
		}
		StringProperty sp = new StringProperty();
		sp.setKey(StringKey.email);
		sp.setValue("ck");
		cook.getStringproperty().add(sp);

		IntegerProperty si = new IntegerProperty();
		si.setKey(IntegerKey.index);
		si.setValue(200);
		cook.getIntegerproperty().add(si);
		JAXB.marshal(cook, new File("text.xml"));
	}
}

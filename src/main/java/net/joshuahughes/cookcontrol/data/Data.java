package net.joshuahughes.cookcontrol.data;

import java.util.LinkedHashSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import ca.odell.glazedlists.BasicEventList;
import net.joshuahughes.cookcontrol.data.property.BooleanProperty;
import net.joshuahughes.cookcontrol.data.property.DateProperty;
import net.joshuahughes.cookcontrol.data.property.FloatProperty;
import net.joshuahughes.cookcontrol.data.property.IntegerProperty;
import net.joshuahughes.cookcontrol.data.property.LongProperty;
import net.joshuahughes.cookcontrol.data.property.StringProperty;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Data <C extends Data<?>>{
	LinkedHashSet<Comment> comment = new LinkedHashSet<>();
    LinkedHashSet<StringProperty> stringproperty = new LinkedHashSet<>();
    LinkedHashSet<LongProperty> longproperty = new LinkedHashSet<>();
    LinkedHashSet<IntegerProperty> integerproperty = new LinkedHashSet<>();
    LinkedHashSet<BooleanProperty> booleanproperty = new LinkedHashSet<>();
    LinkedHashSet<DateProperty> dateproperty = new LinkedHashSet<>();
    LinkedHashSet<FloatProperty> floatproperty = new LinkedHashSet<>();
    public abstract BasicEventList<C> getChildren();
}

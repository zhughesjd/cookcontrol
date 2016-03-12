package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAttribute;

public class BooleanProperty extends Property{
	public static enum BooleanKey{light,vibrate,sound}
	@XmlAttribute
	BooleanKey key;
	@XmlAttribute
	Boolean value;
}

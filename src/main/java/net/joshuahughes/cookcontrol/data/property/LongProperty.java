package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAttribute;

public class LongProperty extends Property{
	public static enum LongKey{utctime,sleep}
	@XmlAttribute
	LongKey key;
	@XmlAttribute
	Long value;
}

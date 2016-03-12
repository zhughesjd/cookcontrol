package net.joshuahughes.cookcontrol.data.property;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

public class DateProperty extends Property{
	public static enum DateKey{creation}
	@XmlAttribute
	DateKey key;
	@XmlAttribute
	Date value;
}

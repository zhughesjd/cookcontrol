package net.joshuahughes.cookcontrol.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import ca.odell.glazedlists.BasicEventList;

@XmlAccessorType(XmlAccessType.FIELD)
public class Thermometer extends Data<Alert>
{
	@XmlAttribute
	int index;
	BasicEventList<Alert> alert = new BasicEventList<>();
	public Thermometer(int index)
	{
		this.index = index;
	}
	public BasicEventList<Alert> getChildren() {
		return alert;
	}
}

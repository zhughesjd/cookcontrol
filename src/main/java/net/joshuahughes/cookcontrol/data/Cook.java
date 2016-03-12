package net.joshuahughes.cookcontrol.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ca.odell.glazedlists.BasicEventList;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Cook extends Data<Thermometer>
{
	BasicEventList<Thermometer> thermometer = new BasicEventList<>();

	@Override
	public BasicEventList<Thermometer> getChildren() {
		return thermometer;
	}

}

package net.joshuahughes.cookcontrol.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "thermometer",propOrder = {"alert"})
public class Thermometer extends Data<Alert>
{
	@XmlAttribute
	int index;
	ArrayList<Alert> alert = new ArrayList<>();
	
	public Thermometer(int index)
	{
		this.index = index;
	}
	@Override
	public ArrayList<Alert> getChildren() {
		return alert;
	}
}

package net.joshuahughes.cookcontrol.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cook",propOrder = {"thermometer"})
@XmlRootElement
public class Cook extends Data<Thermometer>
{
    private ArrayList<Thermometer> thermometer = new ArrayList<>();
	@Override
	public ArrayList<Thermometer> getChildren() {
		return thermometer;
	}
}

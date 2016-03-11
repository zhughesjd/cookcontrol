package net.joshuahughes.cookcontrol.data;

import java.util.ArrayList;
import java.util.Collections;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "alert")
public class Alert extends Data<Alert>{
	@Override
	public ArrayList<Alert> getChildren() {
		return new ArrayList<>(Collections.emptyList());
	}
}

package net.joshuahughes.cookcontrol.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comment")
public class Comment {
	@XmlAttribute
	Date date = new Date(System.currentTimeMillis());
	@XmlValue
	String remark;
	public Comment()
	{
	}
}

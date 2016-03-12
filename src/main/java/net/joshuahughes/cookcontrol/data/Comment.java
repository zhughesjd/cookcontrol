package net.joshuahughes.cookcontrol.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Comment {
	@XmlAttribute
	Date date = new Date(System.currentTimeMillis());
	@XmlValue
	String remark;
	public Comment()
	{
	}
}

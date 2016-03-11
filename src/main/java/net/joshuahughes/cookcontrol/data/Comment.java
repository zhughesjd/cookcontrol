package net.joshuahughes.cookcontrol.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comment")
public class Comment {
	@XmlAttribute
	protected Date creation;
	@XmlAttribute
	protected String remark;
}

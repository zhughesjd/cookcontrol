package net.joshuahughes.cookcontrol.data.property;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import net.joshuahughes.cookcontrol.Key;
import net.joshuahughes.cookcontrol.data.property.LongProperty.LongKey;

@XmlAccessorType(XmlAccessType.FIELD)
public class LongProperty extends Property<Long,LongKey>{
	public static enum LongKey implements Key<Long>{utctime,sleep}
	@XmlAttribute
	LongKey key;
	@XmlAttribute
	Long value;
	public LongKey getKey() {
		return key;
	}
	public void setKey(LongKey key) {
		this.key = key;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
}

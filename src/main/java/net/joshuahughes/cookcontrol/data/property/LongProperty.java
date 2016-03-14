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
	public LongProperty(){}
	public LongProperty(LongKey key, Long value) {
		this.key = key;
		this.value = value;
	}
	public LongKey getKey() {
		return key;
	}
	public LongKey setKey(LongKey newKey) {
		LongKey oldKey = this.key;
		this.key = newKey;
		return oldKey;
	}
	public Long getValue() {
		return value;
	}
	public Long setValue(Long newValue) {
		Long oldValue = this.value;
		this.value = newValue;
		return oldValue;
	}
}

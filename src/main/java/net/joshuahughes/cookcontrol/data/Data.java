package net.joshuahughes.cookcontrol.data;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "element",propOrder = {
	    "comment",
	    "property"
})
public abstract class Data <C extends Data<?>> {
    ArrayList<Comment> comment = new ArrayList<>();
    ArrayList<Property> property = new ArrayList<Property>();
    public abstract ArrayList<C> getChildren();
}

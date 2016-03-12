package net.joshuahughes.cookcontrol.data;

import ca.odell.glazedlists.BasicEventList;

public class Alert extends Data<Alert>{

	@Override
	public BasicEventList<Alert> getChildren() {
		return new BasicEventList<>();
	}
}

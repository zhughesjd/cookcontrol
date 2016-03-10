package net.joshuahughes.smokercontroller.parameters;

import net.joshuahughes.smokercontroller.xml.Platformtype;
import net.joshuahughes.smokercontroller.xml.Smokercontrollertype;
import net.joshuahughes.smokercontroller.xml.Type.Property;

public class SmokerController extends Parameters<Smokercontrollertype,Platform>
{
	private static final long serialVersionUID = 3857793667006092846L;
	public SmokerController(Smokercontrollertype type) throws Exception {
		super(type);
	}
	@Override
	public void init()
	{
		String thisMACAddress = Parameters.getMACAddress();
		String macAddressKey = StringKey.class.getSimpleName()+"."+ StringKey.macaddress;
		boolean insertThisPlatform = true;
		for(Platformtype child : this.type.getPlatform())
			for(Property property : child.getProperty())
				if(property.getKey().equals(macAddressKey) && property.getValue().equals(thisMACAddress))
				{
					insertThisPlatform = false;
					break;
				}
		if(insertThisPlatform)
		{
			Platformtype platformtype = new Platformtype();

			Property property = new Property();
			property.setKey(macAddressKey);
			property.setValue(thisMACAddress);
			platformtype.getProperty().add(property);

			property = new Property();
			property.setKey(StringKey.class.getSimpleName()+"."+ StringKey.label);
			property.setValue(thisMACAddress);
			platformtype.getProperty().add(property);

			this.type.getPlatform().add(platformtype);
		}			
	}
}

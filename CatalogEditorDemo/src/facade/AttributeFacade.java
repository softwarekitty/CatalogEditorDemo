package facade;

import gui.widget.SaveButton;

import org.jdom2.Attribute;

public class AttributeFacade extends AbstractFacade {
	protected Attribute attribute;

	public AttributeFacade(Attribute attribute, SaveButton saveButton,
			VersionFacade vFacade, int ID) {
		super(saveButton, vFacade, ID);
		this.attribute = attribute;
		s = attribute.getValue();
	}

	@Override
	public void sync() {
		attribute.setValue(s);
		handleChange();
	}

	@Override
	public boolean facadeMatchesDocument() {
		return s.equals(attribute.getValue());
	}
	
	public boolean sAsBool(){
		return Boolean.parseBoolean(s);
	}
}

package facade;

import gui.widget.SaveButton;

import org.jdom2.Attribute;


/**
 * The Class AttributeFacade connects s to an attribute.
 */
public class AttributeFacade extends AbstractFacade {
	
	/** The attribute. */
	protected Attribute attribute;

	/**
	 * Instantiates a new attribute facade.
	 *
	 * @param attribute the attribute
	 * @param saveButton the save button
	 * @param vFacade the v facade
	 * @param ID the ID
	 */
	public AttributeFacade(Attribute attribute, SaveButton saveButton,
			VersionFacade vFacade, int ID) {
		super(saveButton, vFacade, ID);
		this.attribute = attribute;
		s = attribute.getValue();
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#sync()
	 */
	@Override
	public void sync() {
		attribute.setValue(s);
		handleChange();
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#facadeMatchesDocument()
	 */
	@Override
	public boolean facadeMatchesDocument() {
		return s.equals(attribute.getValue());
	}
	
	/**
	 * S as bool.
	 *
	 * @return true, if s parses to a true boolean, false otherwise
	 */
	public boolean sAsBool(){
		return Boolean.parseBoolean(s);
	}
}

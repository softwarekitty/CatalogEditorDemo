package facade;

import gui.widget.SaveButton;

import org.jdom2.Attribute;

/**
 * The Class MaxHoursFacade is a custom boolean block that queries the version
 * Facade to find out what state the designator string and the experimental
 * variable are in.
 */
public class MaxHoursFacade extends AttributeFacade implements Sandwichable {

	/**
	 * Instantiates a new max hours facade.
	 * 
	 * @param attribute
	 *            the attribute
	 * @param saveButton
	 *            the save button
	 * @param vFacade
	 *            the v facade
	 * @param ID
	 *            the ID
	 */
	public MaxHoursFacade(Attribute attribute, SaveButton saveButton,
			VersionFacade vFacade, int ID) {
		super(attribute, saveButton, vFacade, ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.Sandwichable#getLeft()
	 */
	public String getLeft() {
		return "No more than ";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.Sandwichable#getRight()
	 */
	public String getRight() {
		return " credits of " + super.vFacade.getDesignator()
				+ super.vFacade.get(AbstractFacade.EXPERIMENTAL).toString()
				+ " may be counted toward graduation.";
	}
}

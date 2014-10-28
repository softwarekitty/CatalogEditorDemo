package facade;

import gui.widget.SaveButton;

import org.jdom2.Attribute;

/**
 * The concept for this class is a representing block of text that either
 * appears or does not appear, which happens a few times, always with
 * attributes.
 */
public class BooleanBlockFacade extends AttributeFacade {

	/** The block content to display if the boolean is true. */
	private String blockContent;

	/**
	 * Instantiates a new boolean block facade.
	 * 
	 * @param attribute
	 *            the attribute
	 * @param saveButton
	 *            the save button
	 * @param vFacade
	 *            the version facade
	 * @param ID
	 *            the ID
	 * @param blockContent
	 *            the block content
	 */
	public BooleanBlockFacade(Attribute attribute, SaveButton saveButton,
			VersionFacade vFacade, int ID, String blockContent) {
		super(attribute, saveButton, vFacade, ID);
		this.blockContent = blockContent;
	}

	/**
	 * Gets the block content.
	 * 
	 * @return the block content
	 */
	public String getBlockContent() {
		return blockContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.AbstractFacade#toString()
	 */
	@Override
	public String toString() {
		return sAsBool() ? blockContent : "";
	}
}

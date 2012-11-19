package facade;

import gui.widget.SaveButton;

import org.jdom2.Attribute;

/*
 * The concept for this class is a representing block of text that either appears or does not appear, 
 * which happens a few times, always with attributes.
 */
public class BooleanBlockFacade extends AttributeFacade {
	private String blockContent;

	public BooleanBlockFacade(Attribute attribute, SaveButton saveButton,
			VersionFacade vFacade, int ID, String blockContent) {
		super(attribute, saveButton, vFacade, ID);
		this.blockContent = blockContent;
	}

	public String getBlockContent() {
		return blockContent;
	}

	@Override
	public String toString() {
		return sAsBool() ? blockContent : "";
	}
}

package block;

import javax.swing.text.Element;

import facade.BooleanBlockFacade;
import facade.StringListener;
import gui.Main;


/**
 * The Class BooleanDocRange represents a block of text backed by a boolean block facade.
 */
public class BooleanDocRange extends AbstractDocRange implements StringListener {
	
	/**
	 * Instantiates a new boolean doc range.
	 *
	 * @param textElement the text element
	 * @param facade the facade
	 * @param ch the ChangeHandler
	 */
	public BooleanDocRange(Element textElement, BooleanBlockFacade facade,
			ChangeHandler ch) {
		super(textElement, facade, ch);
	}

	/**
	 * Facade.
	 *
	 * @return the boolean block facade
	 */
	private BooleanBlockFacade facade() {
		return (BooleanBlockFacade) facade;
	}

	/* (non-Javadoc)
	 * @see block.AbstractDocRange#react(boolean, java.lang.String)
	 */
	@Override
	public void react(boolean hasUnsavedChanges, String s) {
		if (Main.debug3) {
			System.out.println("in BooleanDocRange.react()...eName:"
					+ textElement.getName() + " start: " + start()
					+ " length: " + length());
			System.out.println("in BooelanDocRange.react()..." + " text: "
					+ getText() + " block content: "
					+ facade().getBlockContent());
		}
		boolean textBoolean = facade().getBlockContent().equals(getText());
		boolean facadeBoolean = Boolean.parseBoolean(s);
		if (textBoolean != facadeBoolean) {
			if (Main.debug3) {
				System.out
						.println(" text as boolean is not equal to s as boolean!!!");
			}
			resetEverything();
		}
	}
}

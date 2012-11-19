package block;

import javax.swing.text.Element;

import facade.BooleanBlockFacade;
import facade.StringListener;
import gui.Main;

public class BooleanDocRange extends AbstractDocRange implements StringListener {
	public BooleanDocRange(Element textElement, BooleanBlockFacade facade,
			ChangeHandler ch) {
		super(textElement, facade, ch);
	}

	private BooleanBlockFacade facade() {
		return (BooleanBlockFacade) facade;
	}

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

	@Override
	public int getType() {
		return StringListener.BLOCK;
	}

}

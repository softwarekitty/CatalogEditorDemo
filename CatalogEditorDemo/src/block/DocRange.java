package block;

import java.awt.Color;

import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import undecided.Util;
import facade.AbstractFacade;
import facade.StringListener;
import gui.Main;

public class DocRange extends AbstractDocRange implements StringListener {

	public DocRange(Element textElement, AbstractFacade facade, ChangeHandler ch) {
		super(textElement, facade, ch);
	}

	@Override
	public void react(boolean hasUnsavedChanges, String s) {
		if (Main.debug3) {
			System.out.println("in DocRange.react()...eName:"
					+ textElement.getName() + " start: " + start()
					+ " length: " + length());
			System.out.println("in DocRange.react()..." + " text: " + getText()
					+ " s: " + s);
		}
		if (!getText().equals(s)) {
			if (Main.debug3) {
				System.out.println(" text is not equal to s!!!");
			}
			resetEverything();
		}

		// if the background is still highlighted but shouldn't be, recreate the
		// block
		SimpleAttributeSet a = getAtts();
		Color currentBackground = (Color) a
				.getAttribute(StyleConstants.Background);
		if (!hasUnsavedChanges
				&& currentBackground.equals(Util.colorBook.getHighlight(facade
						.getID()))) {
			if (Main.debug3) {
				System.out.println(" background is highlighted but shouldn't be!!!");
			}
			resetEverything();
		}
	}

	@Override
	public int getType() {
		return StringListener.BLOCK;
	}
}

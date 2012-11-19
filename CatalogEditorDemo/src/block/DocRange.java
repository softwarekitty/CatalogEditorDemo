package block;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import undecided.Util;
import facade.StringFacade;
import facade.StringListener;
import gui.Main;

public class DocRange extends Range implements StringListener {
	private Element textElement;
	private StringFacade facade;
	private ChangeHandler ch;

	public DocRange(Element textElement, StringFacade facade, ChangeHandler ch) {
		super();
		this.textElement = textElement;
		this.facade = facade;
		this.ch = ch;
	}

	// note: DocRanges always have a space in front to keep elements from being
	// removed from the document
	public String getText() {
		try {
			return doc().getText(start(), length());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "ERROR: BAD LOCATION";
		}
	}

	@Override
	public int end() {
		return textElement.getEndOffset();
	}

	@Override
	public int start() {
		return textElement.getStartOffset();
	}

	public StringFacade getFacade() {
		return facade;
	}

	private int length() {
		return end() - start();
	}

	private VersionDocument doc() {
		return (VersionDocument) textElement.getDocument();
	}

	@Override
	public String toString() {
		return "DocRange::" + facade.getName();
	}

	private SimpleAttributeSet getAtts() {
		AttributeSet a = textElement.getAttributes();
		return new SimpleAttributeSet(a);
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
						.getName()))) {
			resetEverything();
		}
	}

	private void resetEverything() {
		// it's pretty important to always remove DocRanges before
		// handling change or you can get listener echo
		removeDocRanges();
		ch.handleChange(getFacade().getName());
	}

	private void removeDocRanges() {
		Thread removingThread = new Thread() {
			public void run() {
				try {
					SwingUtilities.invokeAndWait(new Remover("DocRange"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (Main.debug1) {
					System.out.println("Finished on " + Thread.currentThread());

				}
			}
		};
		removingThread.start();
	}

	class Remover implements Runnable {
		String prefix;

		public Remover(String prefix) {
			this.prefix = prefix;
		}

		@Override
		public void run() {
			facade.removeStringListeners(prefix);
		}

	}

	@Override
	public String getUniqueName() {
		return this.toString();
	}
}

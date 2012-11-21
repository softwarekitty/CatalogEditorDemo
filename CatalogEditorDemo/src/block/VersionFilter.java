package block;

import gui.Main;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * The Class VersionFilter disallows insertion of '\n' and '\t' characters to
 * preserve document order. It also prevents users from deleting constant parts
 * of the text, as determined by VersionDocument.overlapsAConstantRange(Range
 * other).
 */
public class VersionFilter extends DocumentFilter {

	/** The doc. */
	private VersionDocument doc;

	/**
	 * Instantiates a new version filter.
	 * 
	 * @param doc
	 *            the doc
	 */
	public VersionFilter(VersionDocument doc) {
		super();
		this.doc = doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.text.DocumentFilter#insertString(javax.swing.text.DocumentFilter
	 * .FilterBypass, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void insertString(DocumentFilter.FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in insertString");
		}
		StringBuffer buffer = new StringBuffer(string);
		for (int i = buffer.length() - 1; i >= 0; i--) {
			char ch = buffer.charAt(i);
			if (ch == '\n' || ch == '\t') {
				buffer.deleteCharAt(i);
			}
		}
		super.insertString(fb, offset, buffer.toString(), attr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter
	 * .FilterBypass, int, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in replace");
		}
		if (length > 0) {
			fb.remove(offset, length);
		}
		insertString(fb, offset, string, attr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.text.DocumentFilter#remove(javax.swing.text.DocumentFilter
	 * .FilterBypass, int, int)
	 */
	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
			throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in remove: offset: " + offset + " length: "
					+ length + " text to remove: "
					+ doc.getText(offset, length));
		}
		Range insertRange = new Range(offset, offset + length);
		if (doc.overlapsAConstantRange(insertRange)) {
			return;
		}
		super.remove(fb, offset, length);
	}
}
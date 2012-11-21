package field;

import gui.Main;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


/**
 * The Class FieldDocumentFilter filters out newlines and tabs.
 */
public class FieldDocumentFilter extends DocumentFilter {

	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#insertString(javax.swing.text.DocumentFilter.FilterBypass, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void insertString(DocumentFilter.FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in FieldDocumentFilter.insertString");
		}
		StringBuffer buffer = new StringBuffer(string);
		for (int i = buffer.length() - 1; i >= 0; i--) {
			char ch = buffer.charAt(i);
			if (ch == '\n' || ch == '\t') {
				buffer.deleteCharAt(i);
				if (Main.debug3) {
					System.out.println("deleted character");
				}
			}
		}
		super.insertString(fb, offset, buffer.toString(), attr);
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter.FilterBypass, int, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in FieldDocumentFilter.replace");
		}
		if (length > 0)
			fb.remove(offset, length);
		insertString(fb, offset, string, attr);
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#remove(javax.swing.text.DocumentFilter.FilterBypass, int, int)
	 */
	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
			throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in FieldDocumentFilter.remove: offset: "
					+ offset + " length: " + length);
		}
		super.remove(fb, offset, length);
	}

}

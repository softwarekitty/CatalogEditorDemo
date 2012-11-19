package field;

import gui.Main;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FieldDocumentFilter extends DocumentFilter {

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

	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in FieldDocumentFilter.replace");
		}
		if (length > 0)
			fb.remove(offset, length);
		insertString(fb, offset, string, attr);
	}

	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
			throws BadLocationException {
		if (Main.debug3) {
			System.out.println("in FieldDocumentFilter.remove: offset: "
					+ offset + " length: " + length);
		}
		super.remove(fb, offset, length);
	}

}

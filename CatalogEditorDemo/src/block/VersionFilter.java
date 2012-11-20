package block;


import gui.Main;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class VersionFilter extends DocumentFilter {
	private VersionDocument doc;

	public VersionFilter(VersionDocument doc) {
		super();
		this.doc = doc;
	}

	public void insertString(DocumentFilter.FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException {
		if(Main.debug3){
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

	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException {
		if(Main.debug3){
			System.out.println("in replace");	
		}
		if (length > 0){
			fb.remove(offset, length);
		}
		insertString(fb, offset, string, attr);
	}

	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
			throws BadLocationException {
		if(Main.debug3){
			System.out.println("in remove: offset: " + offset + " length: "
					+ length+" text to remove: "+doc.getText(offset, length));
		}
		Range insertRange = new Range(offset,offset+length);
		if(doc.overlapsAConstantRange(insertRange)){
			return;
		}
		super.remove(fb, offset, length);
	}
}
package block;


import gui.Main;

import java.util.LinkedList;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;


public class VersionFilter extends DocumentFilter {
	private VersionDocument doc;

	public VersionFilter(VersionDocument doc) {
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
		if (length > 0)
			fb.remove(offset, length);
		insertString(fb, offset, string, attr);
	}

	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
			throws BadLocationException {
		if(Main.debug3){
			System.out.println("in remove: offset: " + offset + " length: "
					+ length+" text to remove: "+doc.getText(offset, length));
		}
		if (!willDestroyElement(offset, length))
			super.remove(fb, offset, length);
	}

	private boolean willDestroyElement(int offset, int length)
			throws BadLocationException {
		String toBeErased = doc.getText(offset, length);
		if (toBeErased.contains("\n")) {
			if(Main.debug3){
				System.out.println("trying to erase a newline");	
			}
			return true;
		}
		if (toBeErased.contains("\t")) {
			if(Main.debug3){
				System.out.println("trying to erase a tab");	
			}
			return true;
		}
		Range deleteRange = new Range(offset,offset+length);
		LinkedList<Element> elements = doc.getElements();
		for(Element e:elements){
			Range elementRange = new Range(e.getStartOffset(),e.getEndOffset());
			if(elementRange.isContainedIn(deleteRange)){
				if(Main.debug3){
					System.out.println("trying to erase a range that contains an element");
				}
				return true;
			}
		}
		return false;
	}
}
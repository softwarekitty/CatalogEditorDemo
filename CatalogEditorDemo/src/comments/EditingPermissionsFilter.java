package comments;

import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.jdom2.Element;

import undecided.Util;

public class EditingPermissionsFilter extends DocumentFilter {
	private List<Element> editors;
	
	public EditingPermissionsFilter(List<Element> editors){
		this.editors = editors;
	}
	
	public void insertString(DocumentFilter.FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException {
		if(Util.editingIsAllowed(editors)){
			super.insertString(fb, offset, string, attr);
		}
	}

	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException {
		if(Util.editingIsAllowed(editors)){
			if (length > 0)
				fb.remove(offset, length);
			insertString(fb, offset, string, attr);
		}
	}

	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
			throws BadLocationException {
		if(Util.editingIsAllowed(editors)){
			super.remove(fb, offset, length);
		}
	}
}

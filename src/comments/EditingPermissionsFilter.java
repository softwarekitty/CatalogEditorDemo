package comments;

import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.jdom2.Element;

import undecided.Util;


/**
 * The Class EditingPermissionsFilter disallows editors to edit other people's comments.
 */
public class EditingPermissionsFilter extends DocumentFilter {
	
	/** The editors. */
	private List<Element> editors;
	
	/**
	 * Instantiates a new editing permissions filter.
	 *
	 * @param editors the editors
	 */
	public EditingPermissionsFilter(List<Element> editors){
		this.editors = editors;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#insertString(javax.swing.text.DocumentFilter.FilterBypass, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void insertString(DocumentFilter.FilterBypass fb, int offset,
			String string, AttributeSet attr) throws BadLocationException {
		if(Util.editingIsAllowed(editors)){
			super.insertString(fb, offset, string, attr);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter.FilterBypass, int, int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
			String string, AttributeSet attr) throws BadLocationException {
		if(Util.editingIsAllowed(editors)){
			if (length > 0)
				fb.remove(offset, length);
			insertString(fb, offset, string, attr);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.DocumentFilter#remove(javax.swing.text.DocumentFilter.FilterBypass, int, int)
	 */
	public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
			throws BadLocationException {
		if(Util.editingIsAllowed(editors)){
			super.remove(fb, offset, length);
		}
	}
}

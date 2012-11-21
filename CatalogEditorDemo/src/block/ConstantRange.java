package block;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;


/**
 * The Class ConstantRange is a base class for a text range.  Bits of text that should never be modified are represented by this class.
 */
public class ConstantRange extends Range{
	
	/** The text element. */
	private Element textElement;
	
	/**
	 * Instantiates a new constant range.
	 *
	 * @param textElement the text element
	 */
	public ConstantRange(Element textElement){
		this.textElement = textElement;
	}
	
	/* (non-Javadoc)
	 * @see block.Range#end()
	 */
	@Override
	public int end() {
		return textElement.getEndOffset();
	}

	/* (non-Javadoc)
	 * @see block.Range#start()
	 */
	@Override
	public int start() {
		return textElement.getStartOffset();
	}

	/**
	 * Gets the length of the range.
	 *
	 * @return the int
	 */
	protected int length() {
		return end() - start();
	}

	/**
	 * Gets the Document
	 *
	 * @return the version document associated with the text element
	 */
	protected VersionDocument doc() {
		return (VersionDocument) textElement.getDocument();
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		try {
			return doc().getText(start(), length());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "ERROR: BAD LOCATION";
		}
	}
}

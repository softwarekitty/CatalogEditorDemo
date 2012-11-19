package block;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

public class ConstantRange extends Range{
	private Element textElement;
	
	public ConstantRange(Element textElement){
		this.textElement = textElement;
	}
	
	@Override
	public int end() {
		return textElement.getEndOffset();
	}

	@Override
	public int start() {
		return textElement.getStartOffset();
	}

	protected int length() {
		return end() - start();
	}

	protected VersionDocument doc() {
		return (VersionDocument) textElement.getDocument();
	}

	public String getText() {
		try {
			return doc().getText(start(), length());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "ERROR: BAD LOCATION";
		}
	}
}

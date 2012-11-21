package block;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * The Class VersionEditorKit is used to add the document to the JEditorPane in
 * BlockPanel. That could be done another way, so this is not essential. It
 * does, however, provide a nice way to add custom views if desired. That was
 * the original reason for its inclusion and why it could be useful later.
 */
@SuppressWarnings("serial")
public class VersionEditorKit extends StyledEditorKit {

	/** The default factory. */
	ViewFactory defaultFactory = new VersionFactory();

	/** The ch. */
	private ChangeHandler ch;

	/** The colored. */
	private boolean colored;

	/**
	 * Instantiates a new version editor kit.
	 * 
	 * @param ch
	 *            the ch
	 * @param colored
	 *            the colored
	 */
	public VersionEditorKit(ChangeHandler ch, boolean colored) {
		this.ch = ch;
		this.colored = colored;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.text.StyledEditorKit#getViewFactory()
	 */
	public ViewFactory getViewFactory() {
		return defaultFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.text.StyledEditorKit#createDefaultDocument()
	 */
	public Document createDefaultDocument() {
		return new VersionDocument(ch, colored);
	}
}

class VersionFactory implements ViewFactory {
	public View create(javax.swing.text.Element elem) {
		String kind = elem.getName();
		if (kind != null) {
			if (kind.equals(AbstractDocument.ContentElementName)) {
				return new LabelView(elem);
			} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
				return new ParagraphView(elem);
			} else if (kind.equals(AbstractDocument.SectionElementName)) {
				return new BoxView(elem, View.Y_AXIS);
			} else if (kind.equals(StyleConstants.ComponentElementName)) {
				return new ComponentView(elem);
			} else if (kind.equals(StyleConstants.IconElementName)) {
				return new IconView(elem);
			}
		}
		return new LabelView(elem);
	}
}

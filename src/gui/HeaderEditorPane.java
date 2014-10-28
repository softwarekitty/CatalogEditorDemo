package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jdom2.Element;

import facade.ElementFacade;
import field.StringField;
import gui.widget.SaveButton;

/**
 * The Class HeaderEditorPane allows editing of the header element. 
 */
//TODO - allow stylization of the content to reflect the bolding used in the Catalog.
@SuppressWarnings("serial")
public class HeaderEditorPane extends JPanel implements ActionListener {

	/** The Constant HEADER. */
	public static final int HEADER = 99;

	/** The header element. */
	private Element headerElement;

	/** The header field. */
	private StringField headerField;

	/** The save button. */
	private SaveButton saveButton;

	/**
	 * Instantiates a new header editor pane.
	 * 
	 * @param header
	 *            the header
	 */
	public HeaderEditorPane(Element header) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		headerElement = header;
		saveButton = new SaveButton();
		saveButton.setEnabled(false);
		saveButton.addActionListener(this);
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		List<Element> editors = ((Element) headerElement.getParent()).getChild(
				"EDITORS").getChildren("EDITOR");
		ElementFacade ef = new ElementFacade(headerElement, saveButton, null,
				HEADER);
		headerField = new StringField(ef, editors, new Dimension(700, 700),
				false);
		add(headerField);
		add(saveButton);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		saveButton.save();
	}

}

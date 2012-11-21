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
 * The Class ReservedNumbersPane allows editing of RESERVEDNUMBERS element.
 */
@SuppressWarnings("serial")
public class ReservedNumbersPane extends JPanel implements ActionListener{
	
	/** The Constant RESERVEDNUMBERS. */
	public static final int RESERVEDNUMBERS = 363;
	
	/** The reserved numbers element. */
	private Element reservedNumbersElement;
	
	/** The numbers field. */
	private StringField numbersField;
	
	/** The save button. */
	private SaveButton saveButton;
	
	/**
	 * Instantiates a new reserved numbers pane.
	 *
	 * @param reElement the re element
	 */
	public ReservedNumbersPane(Element reElement) {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.reservedNumbersElement = reElement;
		
		saveButton = new SaveButton();
		saveButton.setEnabled(false);
		saveButton.addActionListener(this);
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		List<Element> editors = ((Element) reservedNumbersElement.getParent()).getChild(
				"EDITORS").getChildren("EDITOR");
		ElementFacade ef = new ElementFacade(reservedNumbersElement, saveButton,
				null, RESERVEDNUMBERS);
		numbersField = new StringField(ef,editors, new Dimension(700,200),true);
		add(numbersField);
		add(saveButton);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		saveButton.save();
	}
}

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

@SuppressWarnings("serial")
public class ReservedNumbersPane extends JPanel implements ActionListener{
	public static final int RESERVEDNUMBERS = 363;
	private Element reservedNumbersElement;
	private StringField numbersField;
	private SaveButton saveButton;
	
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
		numbersField = new StringField(ef,editors, new Dimension(700,200));
		add(numbersField);
		add(saveButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		saveButton.save();
	}
}

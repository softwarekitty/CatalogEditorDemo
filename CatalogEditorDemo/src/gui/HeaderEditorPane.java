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
public class HeaderEditorPane extends JPanel implements ActionListener{
	public static final int HEADER = 99;
	private Element headerElement;
	private StringField headerField;
	private SaveButton saveButton;

	public HeaderEditorPane(Element header) {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		headerElement = header;
		saveButton = new SaveButton();
		saveButton.setEnabled(false);
		saveButton.addActionListener(this);
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		List<Element> editors = ((Element) headerElement.getParent()).getChild(
				"EDITORS").getChildren("EDITOR");
		ElementFacade ef = new ElementFacade(headerElement, saveButton,
				null, HEADER);
		headerField = new StringField(ef,editors, new Dimension(700,700));
		add(headerField);
		add(saveButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		saveButton.save();
	}

}

package gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jdom2.Element;

import gui.widget.ElementComponent;
import gui.widget.SaveButton;

@SuppressWarnings("serial")
public class HeaderEditorPane extends JPanel {
	private Element headerElement;
	private ElementComponent headerComponent;
	private SaveButton saveButton;

	public HeaderEditorPane(Element header) {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		headerElement = header;
		saveButton = new SaveButton();
		saveButton.setEnabled(false);
		headerComponent = new ElementComponent(headerElement, saveButton);
		add(headerComponent);
		add(saveButton);
	}

}

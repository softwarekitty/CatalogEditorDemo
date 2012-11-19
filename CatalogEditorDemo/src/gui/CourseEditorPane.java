package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jdom2.Element;

import block.BlockPanel;

import facade.VersionFacade;
import field.FieldPanel;
import gui.widget.SaveButton;

import undecided.Util;

@SuppressWarnings("serial")
public class CourseEditorPane extends JPanel implements ActionListener {
	private SaveButton saveButton;

	public CourseEditorPane(Element course) {
		// put a versionPanel above a save button
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		saveButton = new SaveButton();
		saveButton.addActionListener(this);
		saveButton.setEnabled(false);

		add(getVersionPanel(getCurrentVersionElementFromCourse(course)));
		add(saveButton);
	}

	// gets the versionPanel, which consists of the fieldPanel and the editPanel
	private JPanel getVersionPanel(Element versionElement) {
		
		// the facade mediates multiple views with one save button and one version Element
		VersionFacade facade = new VersionFacade(versionElement, saveButton);
		JPanel versionPanel = new JPanel();
		versionPanel.setLayout(new BoxLayout(versionPanel, BoxLayout.Y_AXIS));
		versionPanel.add(new FieldPanel(facade));
		versionPanel.add(new BlockPanel(facade));
		return versionPanel;
	}

	// this assumes that the parent of the course element is the Program element
	private Element getCurrentVersionElementFromCourse(Element course) {
		String currentYear = Main.getDocument().getRootElement()
				.getAttributeValue("currentYear");
		String programDesignator = course.getParentElement().getAttributeValue(
				"designator");
		String xPathExpression = "//PROGRAM[@designator=\"" + programDesignator
				+ "\"]/COURSE[@number=\"" + course.getAttributeValue("number")
				+ "\"]/VERSION[@catalogYear=\"" + currentYear + "\"]";
		return Util.getElement(xPathExpression);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		saveButton.save();
	}
}

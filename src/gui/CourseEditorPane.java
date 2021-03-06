package gui;

import facade.VersionFacade;
import field.FieldPanel;
import gui.widget.SaveButton;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.jdom2.Element;

import undecided.Util;
import block.BlockPanel;

import comments.CommentsPanel;

/**
 * The Class CourseEditorPane combines the FieldPanel, BlockPanel and
 * CommentsPanel to represent the current version of a course. 
 */
//TODO - allow editing of any version of a course.
@SuppressWarnings("serial")
public class CourseEditorPane extends JPanel implements ActionListener {

	/** The save button. */
	private SaveButton saveButton;

	/** The Constant WIDTH. */
	public static final int WIDTH = 736;

	/**
	 * Instantiates a new course editor pane.
	 * 
	 * @param course
	 *            the course
	 */
	public CourseEditorPane(Element course) {
		// put a versionPanel above a save button
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		saveButton = new SaveButton();
		saveButton.addActionListener(this);
		saveButton.setEnabled(false);
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(getVersionPanel(course));
	}

	// gets the versionPanel, which consists of the fieldPanel and the editPanel
	/**
	 * Gets the version panel.
	 * 
	 * @param courseElement
	 *            the course element
	 * @return the version panel
	 */
	private JPanel getVersionPanel(Element courseElement) {

		String programDesignator = courseElement.getParentElement()
				.getAttributeValue("designator");
		String courseNumber = courseElement.getAttributeValue("number");

		Element versionElement = Util.getCurrentVersionFromCourse(
				courseElement, courseNumber, programDesignator);
		// the facade mediates multiple views with one save button and one
		// version Element
		List<Element> editors = ((Element) courseElement.getParent()).getChild(
				"EDITORS").getChildren("EDITOR");
		VersionFacade facade = new VersionFacade(versionElement,
				programDesignator + " " + courseNumber, saveButton);
		JPanel versionPanel = new JPanel();
		versionPanel.setLayout(new BoxLayout(versionPanel, BoxLayout.Y_AXIS));
//		FieldPanel fp = new FieldPanel(facade, editors);
//		fp.setAlignmentX(LEFT_ALIGNMENT);
//		versionPanel.add(fp);
//		BlockPanel bp = new BlockPanel(facade, editors);
//		bp.setAlignmentX(LEFT_ALIGNMENT);
//		versionPanel.add(bp);
//		versionPanel.add(saveButton);
//		CommentsPanel cp = new CommentsPanel(versionElement.getChild("COMMENTS"),
//				saveButton);
//		cp.setAlignmentX(LEFT_ALIGNMENT);
//		versionPanel.add(cp);
		versionPanel.add(new FieldPanel(facade, editors));
		versionPanel.add(new BlockPanel(facade, editors));
		versionPanel.add(saveButton);
		versionPanel.add(new CommentsPanel(versionElement.getChild("COMMENTS"),
		saveButton));
		return versionPanel;
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

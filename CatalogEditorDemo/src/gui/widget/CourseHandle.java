package gui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jdom2.Element;

import query.Handleable;
import undecided.Util;

/**
 * The Class CourseHandle provides a handle for seeing what courses exist and
 * allows deletion of them.
 */
@SuppressWarnings("serial")
public class CourseHandle extends AbstractHandle implements ActionListener {

	/**
	 * Instantiates a new course handle.
	 * 
	 * @param course
	 *            the course
	 * @param parent
	 *            the parent
	 */
	public CourseHandle(Element course, Handleable parent) {
		super(course, parent);
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		// get the course title
		String programDesignator = course.getParentElement().getAttributeValue(
				"designator");
		String courseNumber = course.getAttributeValue("number");
		Element versionElement = Util.getCurrentVersionFromCourse(course);
		String suffix = Boolean.parseBoolean(versionElement
				.getAttributeValue("experimental")) == true ? "X" : "";
		String courseTitle = programDesignator + " " + courseNumber + suffix;

		JTextArea text = new JTextArea(courseTitle);
		text.setEditable(false);
		container.add(text);
		container.add(deleteButton);

		container.setBackground(Color.WHITE);
		container.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		add(container);
		setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
	}
}

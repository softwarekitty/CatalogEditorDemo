package gui;

import gui.widget.AbstractHandle;
import gui.widget.CourseHandle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jdom2.Element;

import query.Handleable;
import undecided.Util;


/**
 * The Class AddRemoveCoursePane facilitates adding or removing courses from a Program.
 */
//TODO - make adding a course insert it in numerical order!
@SuppressWarnings("serial")
public class AddRemoveCoursePane extends JPanel implements Handleable, ActionListener {
	
	/** The program element. */
	private Element programElement;
	
	/** The input area. */
	private JTextArea inputArea;
	
	/** The add button. */
	private JButton addButton;
	
	/** The container. */
	private JPanel container;

	/**
	 * Instantiates a new AddRemoveCoursePane.
	 *
	 * @param programElement the program element
	 */
	public AddRemoveCoursePane(Element programElement) {
		this.programElement = programElement;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(getAddCoursePane());
		container = new JPanel();
		container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
		for (Element e : programElement.getChildren("COURSE")) {
			CourseHandle handle = new CourseHandle(e, this);
			container.add(handle);
		}
		add(container);
		setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 200));
	}

	/**
	 * Gets the add course pane.
	 *
	 * @return the adds course pane
	 */
	private JPanel getAddCoursePane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		inputArea = new JTextArea();
		inputArea.setPreferredSize(new Dimension(100,25));
		JPanel inputPanel = new JPanel();
		inputPanel.add(new JLabel("add a course with number: "));
		inputPanel.add(inputArea);
		panel.add(inputPanel);
		addButton = new JButton("+");
		addButton.addActionListener(this);
		panel.add(addButton);
		panel.setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 100));
		return panel;
	}

	/* (non-Javadoc)
	 * @see query.Handleable#removeHandle(gui.widget.AbstractHandle)
	 */
	@Override
	public void removeHandle(AbstractHandle toRemove) {
		container.remove(toRemove);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == addButton){
			Element newCourse = new Element("COURSE").addContent(Util.getDefaultVersion());
			newCourse.setAttribute("number",inputArea.getText());
			newCourse.setAttribute("deactivated","false");
			programElement.addContent(newCourse);
			container.add(new CourseHandle(newCourse,this));
			inputArea.setText("");
			Main.repack();
			Main.save();
		}
	}
}

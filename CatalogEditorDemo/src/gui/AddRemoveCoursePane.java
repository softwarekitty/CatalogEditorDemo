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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdom2.Element;

import query.Handleable;
import undecided.Util;


/**
 * The Class AddRemoveCoursePane facilitates adding or removing courses from a Program.
 */
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
		container = getCourseHandles();

		JScrollPane scroller = new JScrollPane(container);
		scroller.setPreferredSize(new Dimension(CourseEditorPane.WIDTH, 800));
		add(scroller);
	}
	
	/**
	 * Gets the course handles.
	 *
	 * @return the course handles
	 */
	private JPanel getCourseHandles(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		for (Element e : programElement.getChildren("COURSE")) {
			CourseHandle handle = new CourseHandle(e, this);
			panel.add(handle);
		}
		return panel;
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
		Main.repack();
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
			Main.repack();
			Main.save();
		}
	}
}

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

import query.ContainerPanel;
import query.Handleable;
import undecided.Util;

@SuppressWarnings("serial")
public class AddRemoveCoursePane extends JPanel implements Handleable, ActionListener {
	private Element programElement;
	private JTextArea inputArea;
	private JButton addButton;
	private JPanel container;

	public AddRemoveCoursePane(Element programElement) {
		this.programElement = programElement;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(getAddCoursePane());
		container = getCourseHandles();

		JScrollPane scroller = new JScrollPane(container);
		scroller.setPreferredSize(new Dimension(CourseEditorPane.WIDTH, 800));
		add(scroller);
	}
	
	private JPanel getCourseHandles(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		for (Element e : programElement.getChildren("COURSE")) {
			CourseHandle handle = new CourseHandle(e, this);
			panel.add(handle);
		}
		return panel;
	}

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

	@Override
	public void removeHandle(AbstractHandle toRemove) {
		container.remove(toRemove);
		Main.repack();
	}

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

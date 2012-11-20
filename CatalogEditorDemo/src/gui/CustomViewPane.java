package gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

import tree.CustomTree;

@SuppressWarnings("serial")
public class CustomViewPane extends JSplitPane {
	public static final int TREE_WIDTH = 240;

	public CustomViewPane(CustomTree tree, JPanel rightPanel) {
		// for Courses with long descriptions, the right panel was getting
		// really long as it was made and then compacting itself so that there
		// was a huge horizontal scrollbar and the initial screen was blank
		// because the content was far to the right and you had to scroll over
		// to see it. The following JScrollPane settings are a mitigation of that problem, but
		// a better solution probably exits
		JScrollPane leftScroll = new JScrollPane(tree);
		leftScroll.setMinimumSize(new Dimension(TREE_WIDTH, Integer.MAX_VALUE));
		setLeftComponent(leftScroll);
		JScrollPane rightScroll = new JScrollPane(rightPanel);
		rightScroll.setMaximumSize(new Dimension(CourseEditorPane.WIDTH,
				Integer.MAX_VALUE));
		rightScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setRightComponent(rightScroll);
	}

}

package gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import tree.CustomTree;

@SuppressWarnings("serial")
public class CustomViewPane extends JSplitPane{
	
	public CustomViewPane(CustomTree tree,JPanel rightPanel){
		setLeftComponent(new JScrollPane(tree));
		setRightComponent(new JScrollPane(rightPanel));
	}

}

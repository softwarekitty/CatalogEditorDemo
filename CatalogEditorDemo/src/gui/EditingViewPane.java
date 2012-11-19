package gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import tree.CustomTree;
import tree.EditingSelectionListener;
import tree.EditingTreeModel;

@SuppressWarnings("serial")
public class EditingViewPane extends JSplitPane{
	
	
	public EditingViewPane(EditingTreeModel model){
		CustomTree tree = new CustomTree(model);
		JPanel rightPanel = new JPanel();
		tree.addTreeSelectionListener(new EditingSelectionListener(tree, rightPanel, model));
		setLeftComponent(new JScrollPane(tree));
		setRightComponent(new JScrollPane(rightPanel));
	}

}

package tree;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;

public abstract class AbstractSelectionListener implements TreeSelectionListener{
	protected JTree tree;
	protected JPanel rightPanel;
	protected CustomTreeModel model;
	
	public AbstractSelectionListener(CustomTree tree, JPanel rightPanel, CustomTreeModel model){
		this.tree = tree;
		this.rightPanel = rightPanel;
		this.model = model;
	}
}

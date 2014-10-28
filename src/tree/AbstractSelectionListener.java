package tree;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;


/**
 * Saves state of the tree, rightPanel and model.
 *
 * @see AbstractSelectionEvent
 */
public abstract class AbstractSelectionListener implements TreeSelectionListener{
	
	/** The tree. */
	protected JTree tree;
	
	/** The right panel. */
	protected JPanel rightPanel;
	
	/** The model. */
	protected CustomTreeModel model;
	
	/**
	 * Instantiates a new abstract selection listener.
	 *
	 * @param tree the tree
	 * @param rightPanel the right panel
	 * @param model the model
	 */
	public AbstractSelectionListener(CustomTree tree, JPanel rightPanel, CustomTreeModel model){
		this.tree = tree;
		this.rightPanel = rightPanel;
		this.model = model;
	}
}

package tree;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

import org.jdom2.Element;

import undecided.Util;


/**
 * The Class CustomTree overrides the JTree method for labeling tree nodes.
 */
@SuppressWarnings("serial")
public class CustomTree extends JTree {
	
	/**
	 * Instantiates a new custom tree.
	 *
	 * @param model the model
	 */
	public CustomTree(TreeModel model){
		super(model);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTree#convertValueToText(java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public String convertValueToText(Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		return Util.getTreeLabel((Element)value);
	}

}

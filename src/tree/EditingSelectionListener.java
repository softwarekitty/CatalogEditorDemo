package tree;

import gui.CourseEditorPane;
import gui.HeaderEditorPane;
import gui.Main;
import gui.StaticViewPane;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;

import org.jdom2.Element;

import undecided.Util;

/**
 * A listener for the Editing view, this triggers the recreation of the right
 * pane depending on what node is selected. Note that an editor's selections are
 * remembered so that upon opening the program again or returning to a given
 * view, the editor returns to the place they were when they left.
 * 
 * @see EditingSelectionEvent
 */
public class EditingSelectionListener extends AbstractSelectionListener {

	/**
	 * Instantiates a new editing selection listener.
	 * 
	 * @param tree
	 *            the tree
	 * @param rightPanel
	 *            the right panel
	 * @param model
	 *            the model
	 */
	public EditingSelectionListener(CustomTree tree, JPanel rightPanel,
			CustomTreeModel model) {
		super(tree, rightPanel, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		if (Main.debug3) {
			System.out.println("event: " + event.toString() + " tree: "
					+ tree.toString() + " rightPanel: " + rightPanel.toString()
					+ " model.tree: " + model.toString());
		}
		Element editor = Main.getEditor();
		Element selectedElement = (Element) tree.getLastSelectedPathComponent();
		if (editor == null) {
			System.err
					.println("null editor in EditingSelectionListener.valueChanged");
		} else {
			String previousTreePath = Main.getEditor().getAttributeValue(
					"editingTreePath");
			String currentTreePath = Util.getXPath(selectedElement);
			if (!currentTreePath.equals(previousTreePath)) {
				Main.setEditorSetting("editingTreePath", currentTreePath);
			}
		}

		if (selectedElement == null) {
			return;
		}

		String tagName = selectedElement.getName();
		if (tagName.equals("COURSE")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new CourseEditorPane(selectedElement));
			Main.repack();
		} else if (tagName.equals("PROGRAM") || tagName.equals("CATALOG")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new StaticViewPane(selectedElement));
			Main.repack();
		} else if (tagName.equals("HEADER")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new HeaderEditorPane(selectedElement));
			Main.repack();
		} else {
			return;
		}
	}
}

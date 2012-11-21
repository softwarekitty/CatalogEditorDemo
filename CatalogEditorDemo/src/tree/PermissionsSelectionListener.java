package tree;

import gui.AddRemoveCoursePane;
import gui.CatalogSettingsPane;
import gui.Main;
import gui.PermissionsPane;
import gui.ReservedNumbersPane;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;

import org.jdom2.Element;

import query.QueriesManagementPane;
import undecided.Util;

/**
 * Used by the Admin view to modify who is an editor of various programs,
 * colleges and who is an admin. Fields like reserved numbers and the current
 * year are also modified by admins. Courses can also be created by admins.
 * Creation and deletion of Colleges and Programs and their header elements has
 * been left out on purpose, as such serious things should not be too easy to
 * do. Note that an admin's selections are remembered so that upon opening the
 * program again or returning to a given view, the admin returns to the place
 * they were when they left.
 * 
 * @see PermissionsSelectionEvent
 */
public class PermissionsSelectionListener extends AbstractSelectionListener {

	/**
	 * Instantiates a new permissions selection listener.
	 * 
	 * @param tree
	 *            the tree
	 * @param rightPanel
	 *            the right panel
	 * @param model
	 *            the model
	 */
	public PermissionsSelectionListener(CustomTree tree, JPanel rightPanel,
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
		Element selectedElement = (Element) tree.getLastSelectedPathComponent();
		if (selectedElement == null) {
			return;
		}
		Element editor = Main.getEditor();
		if (editor == null) {
			System.err
					.println("null editor in EditingSelectionListener.valueChanged");
		} else {
			String previousTreePath = Main.getEditor().getAttributeValue(
					"permissionsTreePath");
			String currentTreePath = Util.getXPath(selectedElement);
			if (!currentTreePath.equals(previousTreePath)) {
				Main.setEditorSetting("permissionsTreePath", currentTreePath);
			}
		}

		String tagName = selectedElement.getName();
		if (tagName.equals("EDITORS")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new PermissionsPane(selectedElement));
			Main.repack();
		} else if (tagName.equals("RESERVEDNUMBERS")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new ReservedNumbersPane(selectedElement));
			Main.repack();
		} else if (tagName.equals("QUERIES")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new QueriesManagementPane(selectedElement));
			Main.repack();
		} else if (tagName.equals("CATALOG")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new CatalogSettingsPane(selectedElement));
			Main.repack();
		} else if (tagName.equals("PROGRAM")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new AddRemoveCoursePane(selectedElement));
			Main.repack();
		} else {
			return;
		}
	}
}

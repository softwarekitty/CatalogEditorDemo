package tree;

import gui.Main;
import gui.CourseEditorPane;
import gui.HeaderEditorPane;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.jdom2.Element;

public class EditingSelectionListener implements TreeSelectionListener{
	private JTree tree;
	private JPanel rightPanel;
	private EditingTreeModel model;

	public EditingSelectionListener(JTree tree, JPanel rightPanel,
			EditingTreeModel model) {
		this.tree = tree;
		this.rightPanel = rightPanel;
		this.model = model;
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		if(Main.debug3){
			System.out.println("event: "+event.toString()+" tree: "+ tree.toString() + " rightPanel: "+ rightPanel.toString()+ " model.tree: "+ model.toString());
		}
		Element selectedElement = (Element) tree.getLastSelectedPathComponent();
		if (selectedElement == null){
			return;
		}
		
		String tagName = selectedElement.getName();
		if (tagName.equals("COURSE")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new CourseEditorPane(selectedElement));
			Main.repack();
		} else if (tagName.equals("HEADER")) {
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new HeaderEditorPane(selectedElement));
			Main.repack();
		} else
			return;
	}
}

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

public class PermissionsSelectionListener extends AbstractSelectionListener{

	
	public PermissionsSelectionListener(CustomTree tree, JPanel rightPanel, CustomTreeModel model){
		super(tree,rightPanel,model);
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
		Element editor = Main.getEditor();
		if(editor==null){
			System.err.println("null editor in EditingSelectionListener.valueChanged");
		}else{
			String previousTreePath = Main.getEditor().getAttributeValue("permissionsTreePath");
			String currentTreePath = Util.getXPath(selectedElement);
			if(!currentTreePath.equals(previousTreePath)){
				Main.setEditorSetting("permissionsTreePath", currentTreePath);	
			}
		}
		
		//colleges can add or subtract college elements, but have to assign a good name attribute
		//college can add or subtract headers if ends up as one
		//programs can add or subtract program elements
		//program can add or subtract course elements - that's it
		
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
		} else if (tagName.equals("QUERIES")){
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new QueriesManagementPane(selectedElement));
			Main.repack();
		}else if(tagName.equals("CATALOG")){
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new CatalogSettingsPane(selectedElement));
			Main.repack();
		}else if(tagName.equals("PROGRAM")){
			rightPanel.removeAll();
			rightPanel.repaint();
			rightPanel.add(new AddRemoveCoursePane(selectedElement));
			Main.repack();
		}else{
			return;
		}
	}

}

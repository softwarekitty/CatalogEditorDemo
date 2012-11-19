package tree;

import java.util.Arrays;
import java.util.LinkedList;

import org.jdom2.Element;


public class EditingTreeModel extends AbstractTreeModel{
	
	private static LinkedList<String> visibleElements = new LinkedList<String>(Arrays.asList("HEADER","PROGRAM","COURSE"));
	private static LinkedList<String> leaves = new LinkedList<String>(Arrays.asList("HEADER","COURSE"));

	//these will be leaf nodes
	@Override
	public boolean isLeaf(Object node) {
		return leaves.contains(((Element)node).getName());
	}

	//these will be visible as leaves or folders
	@Override
	public LinkedList<String> getVisibleElements() {
		return visibleElements;
	}
	

}

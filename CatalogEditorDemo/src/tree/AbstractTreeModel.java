package tree;

import gui.Main;

import java.util.LinkedList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;


/**
 * the two methods not implemented by AbstractTreeModel give extending classes the 
 * freedom to display an xml document with visible nodes nested to any depth within
 * visible nodes, so that the same document may be viewable in different ways.
 *
 */
public abstract class AbstractTreeModel implements TreeModel {

	@Override
	public Object getRoot() {
		return Main.getDocument().getRootElement();
	}

	@Override
	public int getChildCount(Object parent) {
		Element parentElement = (Element) parent;
		LinkedList<Element> allChildren = new LinkedList<Element>(
				parentElement.getChildren());
		if (allChildren.size() == 0) {
			return 0;
		} else {
			int nVisibleChildren = 0;
			LinkedList<String> visibleElements = getVisibleElements();
			for (Element child : allChildren) {
				if (visibleElements.contains(child.getName())) {
					nVisibleChildren++;
				} else {
					nVisibleChildren += getChildCount(child);
				}
			}
			return nVisibleChildren;
		}
	}

	@Override
	public Object getChild(Object parent, int index) {
		Element parentElement = (Element) parent;
		int visibleChildrenPassed = 0;
		LinkedList<Element> allChildren = new LinkedList<Element>(
				parentElement.getChildren());
		LinkedList<String> visibleElements = getVisibleElements();
		for (Element e : allChildren) {
			if (visibleElements.contains(e.getName())) {
				if (index == visibleChildrenPassed) {
					return e;
				} else {
					visibleChildrenPassed++;
				}
			} else if ((getChildCount(e) + visibleChildrenPassed) <= index) {
				visibleChildrenPassed += getChildCount(e);
			} else {
				return getChild(e, index - visibleChildrenPassed);
			}
		}
		System.err
				.println("in getChild, failed to return a child with parent: "
						+ parentElement.getName() + " and index: " + index);
		return null;
	}

	// attempted to use all descendants filtered to get a child at an index, but
	// this does not allow for stopping at next layer

	// Iterator<Element> it = parentElement.getDescendants(getVisibleFilter());
	// while (it.hasNext()) {
	// Element thisElement = it.next();
	// if (index == visibleChildrenPassed) {
	// return thisElement;
	// }
	// visibleChildrenPassed++;
	// }

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Element parentElement = (Element) parent;
		Element childElement = (Element) child;
		int childIndex = 0;
		
		//if the child to search for is not visible, return error
		LinkedList<String> visibleElements = getVisibleElements();
		if (!visibleElements.contains(childElement.getName())) {
			System.err.println("the child: " + ((Element) child).getName()
					+ " is not visible and so has no index");
			return -1;
		}
		
		//iterate through children to find the index of the desired child
		LinkedList<Element> allChildren = new LinkedList<Element>(parentElement.getChildren());
		for (Element e : allChildren) {
			String thisName = e.getName();
			int thisChildCount = getChildCount(e);
			
			//if this is the child or a child on this level is visible...
			if(e.equals(childElement)){
				return childIndex;
			}else if(visibleElements.contains(thisName)){
				childIndex++;
				
				//however, if there are visible children below this level,
				//either find the index on the next visible level of the desired child,
				//or add the number of visible children to the counter and keep looking
			}else if (thisChildCount > 0) {
				int innerIndex = getIndexOfChild(e,child);
				if(innerIndex!=-1){
					return innerIndex + childIndex;
				}else{
					childIndex +=thisChildCount;
				}
			}
		}
		System.err.println("the child: " + ((Element) child).getName()
				+ " was not found in the parent "+parentElement.getName());
		return -1;
	}

	// attempted to use descendats filtered to get the index of a child, but
	// hidden children would trigger that
	// Element parentElement = (Element) parent;
	// int childIndex = 0;
	// Iterator<Element> it = parentElement.getDescendants(getVisibleFilter());
	// while (it.hasNext()) {
	// if (it.next().equals(child)) {
	// return childIndex;
	// }
	// childIndex++;
	// }

	protected ElementFilter getVisibleFilter() {
		LinkedList<String> visibleElements = getVisibleElements();
		ElementFilter visibleFilter = null;
		for (String s : visibleElements) {
			if (visibleFilter == null) {
				visibleFilter = new ElementFilter(s);
			} else {
				visibleFilter = (ElementFilter) visibleFilter
						.or(new ElementFilter(s));
			}
		}
		return visibleFilter;
	}

	public abstract LinkedList<String> getVisibleElements();

	// ******//

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// do nothing
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// do nothing
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// do nothing
	}

}

package query;

import gui.widget.AbstractHandle;


/**
 * The Interface Handleable specifys that a container can remove a Handle from itself.
 */
public interface Handleable {
	
	/**
	 * Removes the handle.
	 *
	 * @param toRemove the to remove
	 */
	public void removeHandle(AbstractHandle toRemove);
	
}

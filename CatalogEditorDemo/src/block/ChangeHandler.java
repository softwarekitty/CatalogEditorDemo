package block;



/**
 * The Interface ChangeHandler exists to isolate the responsibility of handling changes in block ranges.
 */
public interface ChangeHandler {

	/**
	 * Handle change.
	 *
	 * @param trace the trace of who is asking for change to be handled
	 */
	public void handleChange(String trace);
	
	/**
	 * Removes the listener and filter.
	 */
	public void removeListenerAndFilter();
}

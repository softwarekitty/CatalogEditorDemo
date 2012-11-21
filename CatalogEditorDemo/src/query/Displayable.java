package query;


/**
 * The Interface Displayable helps coordinate the display of xqueries.
 */
public interface Displayable {
	
	/**
	 * Gets the display.
	 *
	 * @return the display
	 */
	public DisplayDialog getDisplay();
	
	/**
	 * Sets the display.
	 *
	 * @param display the new display
	 */
	public void setDisplay(DisplayDialog display);

	/**
	 * Display in dialog.
	 *
	 * @return true, if successful
	 */
	public boolean displayInDialog();

	/**
	 * Display.
	 *
	 * @param XMLexpression the xM lexpression
	 */
	public void display(String XMLexpression);
	
}

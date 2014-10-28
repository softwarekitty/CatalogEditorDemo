package facade;

/**
 * The class that is interested in reacting to a change in the String 's'
 * implements this interface, and that class should identify what type of
 * listener it is using the below constants.
 * 
 * @see StringEvent
 */
public interface StringListener {

	/** The Constant BLOCK. */
	public static final int BLOCK = 0;

	/** The Constant FIELD. */
	public static final int FIELD = 1;

	/**
	 * React.
	 * 
	 * @param hasUnsavedChanges
	 *            the has unsaved changes
	 * @param s
	 *            the s
	 */
	public void react(boolean hasUnsavedChanges, String s);

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType();

}

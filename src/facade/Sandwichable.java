package facade;

/**
 * The Interface Sandwichable represents a concept of alterable text sandwiched
 * between left and right constant text. If the alterable text is empty, the
 * constant text should not appear.
 */
public interface Sandwichable {

	/**
	 * Gets the left.
	 * 
	 * @return the left
	 */
	public String getLeft();

	/**
	 * Gets the right.
	 * 
	 * @return the right
	 */
	public String getRight();

	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty();

}

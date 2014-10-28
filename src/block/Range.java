package block;

/**
 * The Class Range is a representation of a numeric range. This class holds some
 * pretty critical logic in the 'isContainedIn' and especially the 'overlaps'
 * methods.
 */
public class Range {

	/** The start. */
	int start;

	/** The end. */
	int end;

	/**
	 * Instantiates a new range, always with start being less or equal to end.
	 * 
	 * @param s
	 *            the s
	 * @param e
	 *            the e
	 */
	public Range(int s, int e) {
		if (s <= e) {
			start = s;
			end = e;
		} else {
			start = e;
			end = s;
		}

	}

	/**
	 * Instantiates a new range with default start and end points being zero.
	 */
	public Range() {
		start = 0;
		end = 0;
	}

	/**
	 * Start.
	 * 
	 * @return the int that this range starts at
	 */
	public int start() {
		return start;
	}

	/**
	 * Sets the start.
	 * 
	 * @param s
	 *            the new starting int value
	 */
	public void setStart(int s) {
		start = s;
	}

	/**
	 * End.
	 * 
	 * @return the int that this range ends at
	 */
	public int end() {
		return end;
	}

	/**
	 * Checks if this range is contained in another range.
	 * 
	 * @param other
	 *            the other range
	 * @return true, if this range is contained in the other range
	 */
	public boolean isContainedIn(Range other) {
		return (other.start() <= start() && other.end() >= end());
	}

	/**
	 * Checks if this range is empty.
	 * 
	 * @return true, if this range is empty
	 */
	public boolean isEmpty() {
		return start() == end();
	}

	/**
	 * Overlaps checks for overlapping ranges, and returns true if any of three
	 * conditions are met: if the starting point or ending point is within the
	 * other range, or if the other range is contained in this range.
	 * 
	 * @param other
	 *            the other
	 * @return true, if ranges overlap each other.
	 */
	public boolean overlaps(Range other) {
		return (other.start() < end() && other.start() > start())
				|| (other.end() > start() && end() > other.end())
				|| other.isContainedIn(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + start() + "," + end() + "]";
	}
}

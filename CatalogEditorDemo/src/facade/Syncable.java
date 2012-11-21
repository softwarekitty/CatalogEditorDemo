package facade;

/**
 * The Interface Syncable helps the save button communicate with facades. If
 * they do not match the document, they should say that they need syncing, and
 * when they sync they should alter the xml document and communicate that they
 * are now in sync by going back to a neutral color.
 */
public interface Syncable {

	/**
	 * Sync.
	 */
	public void sync();

	/**
	 * Needs syncing.
	 * 
	 * @return true, if successful
	 */
	public boolean needsSyncing();

}

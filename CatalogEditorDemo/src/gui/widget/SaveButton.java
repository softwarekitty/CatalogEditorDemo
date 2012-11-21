package gui.widget;

import facade.Syncable;
import gui.Main;

import java.util.ArrayList;

import javax.swing.JButton;

/**
 * The Class SaveButton is a central manager for keeping the in-memory document
 * in sync with what is displayed - or warning the user that it is not in sync.
 * It communicates with facades using the Syncable interface. .
 */
@SuppressWarnings("serial")
public class SaveButton extends JButton {

	/** The list. */
	private ArrayList<Syncable> list;

	/**
	 * Instantiates a new save button.
	 */
	public SaveButton() {
		super("Save");
		list = new ArrayList<Syncable>();
	}

	/**
	 * Adds the to list.
	 * 
	 * @param s
	 *            the s
	 */
	public void addToList(Syncable s) {
		list.add(s);
	}

	/**
	 * Removes the from list.
	 * 
	 * @param s
	 *            the s
	 */
	public void removeFromList(Syncable s) {
		list.remove(s);
	}

	/**
	 * Check all changes.
	 */
	public void checkAllChanges() {
		boolean hasChanges = false;
		for (Syncable s : list) {
			hasChanges = hasChanges || s.needsSyncing();
		}
		this.setEnabled(hasChanges);
	}

	/**
	 * Save.
	 */
	public void save() {
		// synch field with in-memory document, set field to appear saved
		for (Syncable s : list) {
			s.sync();
		}

		// call the method in Main
		Main.save();
	}

}

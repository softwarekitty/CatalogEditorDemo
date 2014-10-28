package facade;

import gui.Main;
import gui.widget.SaveButton;

import java.awt.Color;

import undecided.Util;

/**
 * The Class AbstractFacade takes care of listeners and the save button,
 * getting/setting s (which will usually correspond to some part of an XML
 * document), getting color, name and ID for a facade asks implementing classes
 * to determine if the facade matches the document and syncing the document
 * (when requested). Note that the toString() method is used by the
 * VersionDocument to determine what content to add.
 * 
 */
public abstract class AbstractFacade implements Syncable {
	/*
	 * In order for the Document Range concept to work, there can only be one
	 * range of text for every facade. The following constants represent the
	 * possible ranges of text that might be in a text block. Some of them may
	 * be absent, it is inconvenient for the AbstractFacade to try to know. It
	 * must be ready to represent a document that manifests all available
	 * document ranges.
	 */
	/** The Constant EXPERIMENTAL. */
	public static final int EXPERIMENTAL = 0;// boolean attribute

	/** The Constant TITLE. */
	public static final int TITLE = 1; // string element

	/** The Constant DUAL. */
	public static final int DUAL = 2; // sandwich element

	/** The Constant CROSS. */
	public static final int CROSS = 3;// sandwich element

	/** The Constant PRIMARY. */
	public static final int PRIMARY = 4; // string element

	/** The Constant SECONDARY. */
	public static final int SECONDARY = 5;// string element

	/** The Constant CREDIT. */
	public static final int CREDIT = 6; // string element

	/** The Constant REPEATABLE. */
	public static final int REPEATABLE = 7;// boolean attribute

	/** The Constant MAXHOURS. */
	public static final int MAXHOURS = 8;// custom sandwich element

	/** The Constant FALL. */
	public static final int FALL = 9;// custom object

	/** The Constant SPRING. */
	public static final int SPRING = 10;// custom object

	/** The Constant SUMMER. */
	public static final int SUMMER = 11; // custom object

	/** The Constant PREREQ. */
	public static final int PREREQ = 12;// sandwich element

	/** The Constant DESCRIPTION. */
	public static final int DESCRIPTION = 13;// string element

	/** The Constant NONMAJORGC. */
	public static final int NONMAJORGC = 14;// boolean attribute

	/** The Constant SFONLY. */
	public static final int SFONLY = 15;// boolean attribute

	/** The Constant NOTES. */
	public static final int NOTES = 16;// string element

	/** The Constant allConstants. */
	public static final int[] allConstants = { EXPERIMENTAL, TITLE, DUAL,
			CROSS, PRIMARY, SECONDARY, CREDIT, REPEATABLE, MAXHOURS, FALL,
			SPRING, SUMMER, PREREQ, DESCRIPTION, NONMAJORGC, SFONLY, NOTES };

	// to simplify listener management, field and block listeners are handled
	// separately
	/** The listeners. */
	private StringListener[] listeners;

	/** The has unsaved changes. */
	protected boolean hasUnsavedChanges;

	/** The save button. */
	protected SaveButton saveButton;

	/** The version facade. */
	protected VersionFacade vFacade;

	/**
	 * The string representing the xml content (except for OfferingFacades which
	 * are complex).
	 */
	protected String s;

	/** The ID. */
	protected int ID;

	/**
	 * Instantiates a new abstract facade.
	 * 
	 * @param saveButton
	 *            the save button
	 * @param vFacade
	 *            the v facade
	 * @param ID
	 *            the ID
	 */
	public AbstractFacade(SaveButton saveButton, VersionFacade vFacade, int ID) {
		this.vFacade = vFacade;
		this.ID = ID;
		this.saveButton = saveButton;
		hasUnsavedChanges = false;

		saveButton.addToList(this);
		listeners = new StringListener[2];
	}

	/**
	 * Facade matches document.
	 * 
	 * @return true, if successful
	 */
	public abstract boolean facadeMatchesDocument();

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.Syncable#sync()
	 */
	public abstract void sync();

	/**
	 * Gets the s.
	 * 
	 * @return the s
	 */
	public synchronized String getS() {
		return s;
	}

	/**
	 * Sets the s.
	 * 
	 * @param s
	 *            the new s
	 */
	public synchronized void setS(String s) {
		this.s = s;
		handleChange();
	}

	/**
	 * Gets the ID.
	 * 
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return "".equals(toString());
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return Util.getNameFromID(ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.Syncable#needsSyncing()
	 */
	public boolean needsSyncing() {
		return hasUnsavedChanges;
	}

	/**
	 * Handle change.
	 */
	public void handleChange() {
		if (facadeMatchesDocument()) {
			hasUnsavedChanges = false;
			saveButton.checkAllChanges();
		} else {
			hasUnsavedChanges = true;
			saveButton.setEnabled(true);
		}
		for (StringListener sl : listeners) {
			if (sl != null) {
				if (Main.debug3) {
					System.out.println("notifying Stringlistener of type: "
							+ sl.getType());
				}
				sl.react(hasUnsavedChanges, s);
			}
		}
	}

	/**
	 * Gets the block color.
	 * 
	 * @return the block color
	 */
	public Color getBlockColor() {
		if (needsSyncing()) {
			return Util.colorBook.getBackground(ID);
		} else {
			return Util.colorBook.getHighlight(ID);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getS();
	}

	/**
	 * Calls the version facade to remove all the block listeners.
	 */
	public void removeAll() {
		vFacade.removeAllBlockStringListeners();
	}

	/**
	 * Removes the block string listener from this facade.
	 */
	public void removeBlockStringListener() {
		if (Main.debug3) {
			System.out.println("removing " + getName() + " block listener");
		}
		listeners[StringListener.BLOCK] = null;
	}

	/**
	 * Adds the given string listener.
	 * 
	 * @param sl
	 *            the StringListener to add
	 */
	public synchronized void addStringListener(StringListener sl) {
		if (Main.debug3) {
			System.out.println("adding " + getName() + " listener of type "
					+ sl.getType());
		}
		listeners[sl.getType()] = sl;
	}

	/**
	 * Checks for block listener.
	 * 
	 * @return true, if there is a block listener
	 */
	public boolean hasBlockListener() {
		return listeners[StringListener.BLOCK] != null;
	}
}

package facade;

import gui.Main;
import gui.widget.SaveButton;

import java.awt.Color;

import undecided.Util;

/*
 * Takes care of listeners and the save button, 
 * getting/setting s (which should correspond to some part of an XML document),
 * getting color, name and ID for a facade
 * asks implementing classes to determine if the facade matches the document and
 * syncing the document (when requested)
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

	public static final int EXPERIMENTAL = 0;// boolean attribute
	public static final int TITLE = 1; // string element
	public static final int DUAL = 2; // sandwich element
	public static final int CROSS = 3;// sandwich element
	public static final int PRIMARY = 4; // string element
	public static final int SECONDARY = 5;// string element
	public static final int CREDIT = 6; // string element
	public static final int REPEATABLE = 7;// boolean attribute
	public static final int MAXHOURS = 8;// custom sandwich element
	public static final int FALL = 9;// custom object handling presence/absence of a string element with 3 possible attributes
	public static final int SPRING = 10;// custom object handling presence/absence of a string element with 3 possible attribu
	public static final int SUMMER = 11; // custom object handling presence/absence of a string element with 3 possible attributes
	public static final int PREREQ = 12;// sandwich element
	public static final int DESCRIPTION = 13;// string element
	public static final int NONMAJORGC = 14;// boolean attribute
	public static final int SFONLY = 15;// boolean attribute
	public static final int NOTES = 16;// string element
	public static final int[] allConstants = { EXPERIMENTAL, TITLE, DUAL, CROSS,
			PRIMARY, SECONDARY, CREDIT, REPEATABLE, MAXHOURS, FALL, SPRING,
			SUMMER, PREREQ, DESCRIPTION, NONMAJORGC, SFONLY, NOTES };
	
	//to simplify listener management, field and block listeners are handled separately
	private StringListener[] listeners;

	protected boolean hasUnsavedChanges;
	protected SaveButton saveButton;
	protected VersionFacade vFacade;
	protected String s;
	protected int ID;

	public AbstractFacade(SaveButton saveButton, VersionFacade vFacade,int ID) {
		this.vFacade = vFacade;
		this.ID = ID;
		this.saveButton = saveButton;
		hasUnsavedChanges = false;
		
		saveButton.addToList(this);
		listeners = new StringListener[2];
	}
	
	public abstract boolean facadeMatchesDocument();

	public abstract void sync();
	
	public synchronized String getS() {
		return s;
	}

	public synchronized void setS(String s) {
		this.s = s;
		handleChange();
	}
	
	public int getID(){
		return ID;
	}
	
	public boolean isEmpty(){
		return "".equals(toString());
	}

	public String getName(){
		return Util.getNameFromID(ID);
	}

	public boolean needsSyncing() {
		return hasUnsavedChanges;
	}

	public void handleChange() {
		if (facadeMatchesDocument()) {
			hasUnsavedChanges = false;
			saveButton.checkAllChanges();
		} else {
			hasUnsavedChanges = true;
			saveButton.setEnabled(true);
		}
		for (StringListener sl : listeners) {
			if(sl!=null){
				if(Main.debug3){
					System.out.println("notifying Stringlistener of type: "+sl.getType());
				}
				sl.react(hasUnsavedChanges, s);
			}
		}
	}

	public Color getBlockColor() {
		if (needsSyncing()) {
			return Util.colorBook.getBackground(ID);
		} else {
			return Util.colorBook.getHighlight(ID);
		}
	}
	
	@Override
	public String toString(){
		return getS();
	}
	
	public void removeAll() {
		vFacade.removeAllBlockStringListeners();
	}
	
	public void removeBlockStringListener(){
		if(Main.debug3){
			System.out.println("removing "+getName()+" block listener");
		}
		listeners[StringListener.BLOCK]=null;
	}
	
	public synchronized void addStringListener(StringListener sl) {
		if(Main.debug3){
			System.out.println("adding "+getName()+" listener of type "+sl.getType());
		}
		listeners[sl.getType()]=sl;
	}
}


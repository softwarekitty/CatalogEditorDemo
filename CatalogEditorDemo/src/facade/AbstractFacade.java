package facade;


import gui.widget.SaveButton;

import java.awt.Color;

import org.jdom2.Element;

import undecided.Util;


public abstract class AbstractFacade implements Synchable{
	protected Element element;
	protected boolean hasUnsavedChanges;
	protected SaveButton saveButton;
	private VersionFacade vFacade;
	
	public AbstractFacade(Element element,SaveButton saveButton,VersionFacade vFacade){
		this.element = element;
		this.vFacade = vFacade;
		hasUnsavedChanges = false;
		this.saveButton = saveButton;
		saveButton.addToList(this);
	}
	
	@Override
	public abstract void synch();
	
	public abstract void removeListenersWithPrefix(String prefix);


	@Override
	public boolean needsSynching() {
		return hasUnsavedChanges;
	}
	
	public String getName(){
		return element.getName();
	}
	
	public void removeStringListeners(String prefix){
		vFacade.removeListeners(prefix);
	}
	
	public Color getBlockColor(){
		if(needsSynching()){
			return Util.colorBook.getHighlight(getName());
		}else{
			return Util.colorBook.getTransparent(getName());
		}
	}

}

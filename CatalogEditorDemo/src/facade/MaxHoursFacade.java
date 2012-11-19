package facade;

import gui.widget.SaveButton;

import org.jdom2.Attribute;

public class MaxHoursFacade extends AttributeFacade implements Sandwichable{

	public MaxHoursFacade(Attribute attribute, SaveButton saveButton,
			VersionFacade vFacade, int ID) {
		super(attribute, saveButton, vFacade, ID);
	}
	
	public String getLeft(){
		return "No more than ";
	}
	
	public String getRight(){
		return " credits of "+super.vFacade.getDesignator()
		+ super.vFacade.get(AbstractFacade.EXPERIMENTAL).toString()
		+ " may be counted toward graduation.";
	}
}

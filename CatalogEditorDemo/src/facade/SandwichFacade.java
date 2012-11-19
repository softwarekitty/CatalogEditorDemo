package facade;

import org.jdom2.Element;

import gui.widget.SaveButton;

public class SandwichFacade extends ElementFacade implements Sandwichable{
	private String left;
	private String right;
	
	
	public SandwichFacade(Element element, SaveButton saveButton,
			VersionFacade vFacade, int ID,String left,String right) {
		super(element, saveButton, vFacade, ID);
		this.left = left;
		this.right = right;
	}
	
	public String getLeft(){
		return left;
	}
	
	public String getRight(){
		return right;
	}
	
}

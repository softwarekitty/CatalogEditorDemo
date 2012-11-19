package facade;


import gui.widget.SaveButton;

import org.jdom2.Element;


public class ElementFacade extends AbstractFacade {
	private Element element;
	


	public ElementFacade(Element element, SaveButton saveButton,VersionFacade vFacade,int ID) {
		super(saveButton,vFacade,ID);
		this.element = element;
		s = element.getText();
	}
	
	public String getName(){
		return element.getName();
	}
	
	@Override
	public void sync() {
		element.setText(s);
		handleChange();
	}

	@Override
	public boolean facadeMatchesDocument() {
		return s.equals(element.getText());
	}

	@Override
	public int getID() {
		return ID;
	}
}

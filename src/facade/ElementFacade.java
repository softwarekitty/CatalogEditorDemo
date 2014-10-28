package facade;


import gui.widget.SaveButton;

import org.jdom2.Element;



/**
 * The Class ElementFacade relates s to an element.
 */
public class ElementFacade extends AbstractFacade {
	
	/** The element. */
	private Element element;
	


	/**
	 * Instantiates a new element facade.
	 *
	 * @param element the element
	 * @param saveButton the save button
	 * @param vFacade the v facade
	 * @param ID the ID
	 */
	public ElementFacade(Element element, SaveButton saveButton,VersionFacade vFacade,int ID) {
		super(saveButton,vFacade,ID);
		this.element = element;
		s = element.getText();
	}
	
	/* (non-Javadoc)
	 * @see facade.AbstractFacade#getName()
	 */
	public String getName(){
		return element.getName();
	}
	
	/* (non-Javadoc)
	 * @see facade.AbstractFacade#sync()
	 */
	@Override
	public void sync() {
		element.setText(s);
		handleChange();
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#facadeMatchesDocument()
	 */
	@Override
	public boolean facadeMatchesDocument() {
		return s.equals(element.getText());
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#getID()
	 */
	@Override
	public int getID() {
		return ID;
	}
}

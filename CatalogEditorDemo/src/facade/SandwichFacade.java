package facade;

import org.jdom2.Element;

import gui.widget.SaveButton;


/**
 * The Class SandwichFacade manifests the sandwich concept for facades with element backing.
 */
public class SandwichFacade extends ElementFacade implements Sandwichable{
	
	/** The left. */
	private String left;
	
	/** The right. */
	private String right;
	
	
	/**
	 * Instantiates a new sandwich facade.
	 *
	 * @param element the element
	 * @param saveButton the save button
	 * @param vFacade the v facade
	 * @param ID the ID
	 * @param left the left
	 * @param right the right
	 */
	public SandwichFacade(Element element, SaveButton saveButton,
			VersionFacade vFacade, int ID,String left,String right) {
		super(element, saveButton, vFacade, ID);
		this.left = left;
		this.right = right;
	}
	
	/* (non-Javadoc)
	 * @see facade.Sandwichable#getLeft()
	 */
	public String getLeft(){
		return left;
	}
	
	/* (non-Javadoc)
	 * @see facade.Sandwichable#getRight()
	 */
	public String getRight(){
		return right;
	}
	
}

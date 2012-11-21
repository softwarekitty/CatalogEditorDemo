package undecided;

import java.util.HashMap;

import facade.AbstractFacade;


/**
 * The Class NameBook is a lookup table mapping names to ID integers.
 */
@SuppressWarnings("serial")
public class NameBook extends HashMap<String, Integer>{

	/**
	 * Instantiates a new name book.
	 */
	public NameBook(){
		super();
		for(int ID:AbstractFacade.allConstants){
			put(Util.getNameFromID(ID),ID);
		}
	}
	
}

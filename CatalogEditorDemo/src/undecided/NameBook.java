package undecided;

import java.util.HashMap;

import facade.AbstractFacade;

@SuppressWarnings("serial")
public class NameBook extends HashMap<String, Integer>{

	public NameBook(){
		super();
		for(int ID:AbstractFacade.allConstants){
			put(Util.getNameFromID(ID),ID);
		}
	}
	
}

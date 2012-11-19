package gui.widget;

import facade.Synchable;
import gui.Main;

import java.util.ArrayList;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SaveButton extends JButton{
	private ArrayList<Synchable> list;
	
	public SaveButton(){
		super("Save");
		list = new ArrayList<Synchable>();
	}
	
	public void addToList(Synchable s){
		list.add(s);
	}
	
	public void checkAllChanges(){
		boolean hasChanges = false;
		for(Synchable s: list){
			hasChanges = hasChanges || s.needsSynching();
		}
		this.setEnabled(hasChanges);
	}
	
	public void save(){
		//synch field with in-memory document, set field to appear saved
		for(Synchable s: list){
			s.synch();
		}
		
		//call the method in Main
		Main.save();
	}
	
}

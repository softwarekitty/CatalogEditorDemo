package gui.widget;

import facade.Syncable;
import gui.Main;

import java.util.ArrayList;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class SaveButton extends JButton{
	private ArrayList<Syncable> list;
	
	public SaveButton(){
		super("Save");
		list = new ArrayList<Syncable>();
	}
	
	public void addToList(Syncable s){
		list.add(s);
	}
	
	public void removeFromList(Syncable s){
		list.remove(s);
	}
	
	public void checkAllChanges(){
		boolean hasChanges = false;
		for(Syncable s: list){
			hasChanges = hasChanges || s.needsSyncing();
		}
		this.setEnabled(hasChanges);
	}
	
	public void save(){
		//synch field with in-memory document, set field to appear saved
		for(Syncable s: list){
			s.sync();
		}
		
		//call the method in Main
		Main.save();
	}
	
}

package facade;


import gui.Main;
import gui.widget.SaveButton;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom2.Element;


public class StringFacade extends AbstractFacade {
	private String s;
	private ArrayList<StringListener> listeners;


	public StringFacade(Element element, SaveButton saveButton,VersionFacade vFacade) {
		super(element, saveButton,vFacade);
		s = element.getText();
		listeners = new ArrayList<StringListener>();
	}
	
	public synchronized void addStringListener(StringListener sl){
		if(Main.debug5){
			System.out.println("adding StringListener with name: "+sl.getUniqueName());
		}
		listeners.add(sl);
	}
	
	public synchronized void removeStringListener(StringListener sl){
		if(Main.debug5){
			System.out.println(" before removing listener, list size is: "+listeners.size());
		}
		listeners.remove(sl);
		if(Main.debug5){
			System.out.println(" removed listener, now list size is: "+listeners.size());
		}
	}
	
	public synchronized boolean containsListener(StringListener sl){
		return listeners.contains(sl);
	}

	public String getS() {
		return s;
	}

	public synchronized void setS(String s) {
		this.s = s;
		handleChange();
	}

	@Override
	public void synch() {
		element.setText(s);
		handleChange();
	}

	private synchronized void handleChange() {
		if (s.equals(element.getText())) {
			hasUnsavedChanges = false;
			saveButton.checkAllChanges();
		} else {
			hasUnsavedChanges = true;
			saveButton.setEnabled(true);
		}
		for(StringListener sl:listeners){
			sl.react(hasUnsavedChanges, s);
		}
	}

	@Override
	public synchronized void removeListenersWithPrefix(String prefix) {
		if(Main.debug5){
			System.out.println(" before removing listeners with prefix: "+prefix+", list size is: "+listeners.size());
		}
		Iterator<StringListener> it = listeners.iterator();
		while(it.hasNext()){
			StringListener listener = it.next();
			if(listener.getUniqueName().startsWith(prefix)){
				it.remove();
			}
		}
		if(Main.debug5){
			System.out.println(" removed listeners with prefix: "+prefix+", now list size is: "+listeners.size());
		}
		

	}

}

package facade;

public interface StringListener {
	
	public void react(boolean hasUnsavedChanges,String s);
	
	public String getUniqueName();

}

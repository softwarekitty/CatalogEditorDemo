package block;


public interface ChangeHandler {

	public void handleChange(String trace);
	
	public void removeListenerAndFilter();
}

package query;

public interface Handleable {
	
	public void removeHandle(QueryHandle toRemove);

	public DisplayDialog getDisplay();
	
	public void setDisplay(DisplayDialog display);

	public boolean displayInDialog();

	public void display(String XMLexpression);
	
}

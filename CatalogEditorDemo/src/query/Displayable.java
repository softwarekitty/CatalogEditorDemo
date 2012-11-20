package query;

public interface Displayable {
	
	public DisplayDialog getDisplay();
	
	public void setDisplay(DisplayDialog display);

	public boolean displayInDialog();

	public void display(String XMLexpression);
	
}

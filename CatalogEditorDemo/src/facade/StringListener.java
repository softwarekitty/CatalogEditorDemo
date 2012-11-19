package facade;

public interface StringListener {
	public static final int BLOCK = 0;
	public static final int FIELD = 1;
	
	public void react(boolean hasUnsavedChanges,String s);
	
	public int getType();

}

package query;

import javax.swing.BoxLayout;
import javax.swing.JPanel;


/**
 * The Class ContainerPanel is a convenience of a JPanel that has an accessible container.  
 */
//TODO - use more or get rid of this
@SuppressWarnings("serial")
public class ContainerPanel extends JPanel{
	
	/** The container. */
	private JPanel container;
	
	/**
	 * Instantiates a new container panel.
	 */
	public ContainerPanel(){
		container = new JPanel();
		container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
	}
	
	/**
	 * Gets the container panel.
	 *
	 * @return the container panel
	 */
	public JPanel getContainerPanel(){
		return container;
	}
}

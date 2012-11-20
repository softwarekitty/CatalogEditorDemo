package query;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ContainerPanel extends JPanel{
	private JPanel container;
	
	public ContainerPanel(){
		container = new JPanel();
		container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
	}
	
	public JPanel getContainerPanel(){
		return container;
	}
}

package query;

import gui.Main;
import gui.widget.AbstractHandle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.jdom2.Element;


/**
 * The Class QueryHandle is used in managing queries to delete queries, or to set them as global - if allowed.
 */
@SuppressWarnings("serial")
public class QueryHandle extends AbstractHandle implements ActionListener {
	
	/** The container. */
	private JPanel container;
	
	/** The global button. */
	private JButton globalButton;
	
	/** The play button. */
	private JButton playButton;
	
	/** The is global. */
	private boolean isGlobal;
	
	/** The control global. */
	private boolean controlGlobal;

	/**
	 * Instantiates a new query handle.
	 *
	 * @param element the element
	 * @param parent the parent
	 */
	public QueryHandle(Element element, Handleable parent) {
		this(element, parent, true, true);
	}

	/**
	 * Instantiates a new query handle.
	 *
	 * @param element the element
	 * @param parent the parent
	 * @param controlGlobal the control global
	 * @param controlDelete the control delete
	 */
	public QueryHandle(Element element, Handleable parent,
			boolean controlGlobal, boolean controlDelete) {
		super(element,parent);
		this.controlGlobal = controlGlobal;

		isGlobal = Boolean.parseBoolean(element
				.getAttributeValue("global"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		playButton = new JButton(new ImageIcon("play_icon.jpg"));
		playButton.addActionListener(this);
		playButton.setBorder(BorderFactory.createEmptyBorder());
		playButton.setContentAreaFilled(false);
		container.add(playButton);

		JTextArea text = new JTextArea(element.getText());
		text.setEditable(false);
		text.setToolTipText(element.getAttributeValue("description"));
		container.add(text);

		if(controlGlobal){
			globalButton = new JButton(new ImageIcon("icon-globe.jpg"));
			globalButton.addActionListener(this);
			globalButton.setBorder(BorderFactory.createEmptyBorder());
			globalButton.setContentAreaFilled(false);
			container.add(globalButton);
		}

		if(controlDelete){
			container.add(deleteButton);
		}


		container.setBackground(Color.WHITE);
		container.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, getBorderColor()));
		add(container);
		setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
	}

	/**
	 * Gets the border color.
	 *
	 * @return the border color
	 */
	private Color getBorderColor() {
		return isGlobal && controlGlobal ? Color.GREEN : UIManager
				.getColor("Panel.background");
	}

	/* (non-Javadoc)
	 * @see gui.widget.AbstractHandle#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		 if (event.getSource() == globalButton && controlGlobal) {
			String message = isGlobal ? "Really Remove Global Status?"
					: "Really Add Global Status?";
			int reply = JOptionPane.showConfirmDialog(null, message,
					"Set Global", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				isGlobal = !isGlobal;
				element.setAttribute("global", Boolean.valueOf(isGlobal)
						.toString());
				setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
						getBorderColor()));
				Main.repack();
				Main.save();
			}
		} else if (event.getSource() == playButton) {
			Displayable d = (Displayable)parent;
			if (d.displayInDialog()) {
				DisplayDialog display = d.getDisplay();
				if (display != null) {
					display.dispose();
				}
				d.setDisplay(new DisplayDialog(Main.getFrame(),
						element.getAttributeValue("description"),
						element.getText()));
			} else {
				d.display(element.getText());
			}

		}
	}
}

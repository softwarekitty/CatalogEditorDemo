package gui.widget;

import gui.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom2.Element;

import query.Handleable;

/**
 * A handle provides a gui for seeing something about an element (perhaps just
 * that it exists) and optionally deleting an element from both the underlying
 * database and the view
 */
@SuppressWarnings("serial")
public abstract class AbstractHandle extends JPanel implements ActionListener {

	/** The element. */
	protected Element element;

	/** The parent. */
	protected Handleable parent;

	/** The delete button. */
	protected JButton deleteButton;

	/**
	 * Instantiates a new abstract handle.
	 * 
	 * @param element
	 *            the element
	 * @param parent
	 *            the parent
	 */
	public AbstractHandle(Element element, Handleable parent) {
		this.element = element;
		this.parent = parent;
		this.deleteButton = initializeDeleteButton();
	}

	/**
	 * Initialize delete button.
	 * 
	 * @return the j button
	 */
	private JButton initializeDeleteButton() {
		JButton deleteButton = new JButton(new ImageIcon("icon_x.gif"));
		deleteButton.addActionListener(this);
		deleteButton.setBorder(BorderFactory.createEmptyBorder());
		deleteButton.setContentAreaFilled(false);
		return deleteButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == deleteButton) {
			String elementName = element.getName();
			elementName = elementName.charAt(0)
					+ elementName.substring(1).toLowerCase();
			int reply = JOptionPane.showConfirmDialog(null, "Confirm Delete",
					"Really Delete " + elementName + "?",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				element.getParentElement().removeContent(element);
				parent.removeHandle(this);
				Main.repack();
				Main.save();
			}
		}
	}
}

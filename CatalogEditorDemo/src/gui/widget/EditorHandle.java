package gui.widget;

import gui.Main;
import gui.PermissionsPane;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom2.Element;


/**
 * The Class EditorHandle allows for the viewing and deletion of Editor Elements.
 */
@SuppressWarnings("serial")
public class EditorHandle extends JPanel implements ActionListener {
	
	/** The editor element. */
	private Element editorElement;
	
	/** The parent. */
	private PermissionsPane parent;
	
	/** The delete button. */
	private JButton deleteButton;

	/**
	 * Instantiates a new editor handle.
	 *
	 * @param editorElement the editor element
	 * @param parent the parent
	 */
	public EditorHandle(Element editorElement, PermissionsPane parent) {
		this.editorElement = editorElement;
		this.parent = parent;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		JLabel editor = new JLabel(editorElement.getAttributeValue("netID"));
		editor.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.LIGHT_GRAY));
		add(editor);

		deleteButton = new JButton("x");
		deleteButton.addActionListener(this);
		add(deleteButton);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == deleteButton) {
			if(Main.debug2){
				System.out.println(" x button clicked for editor with netID: "+editorElement.getAttributeValue("netID"));
			}
			int reply = JOptionPane.showConfirmDialog(null, "Confirm Delete", "Really Delete "
					+ editorElement.getAttributeValue("netID") + "?",
					JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				if(Main.debug2){
					System.out.println("yes option selected");
				}
				editorElement.getParentElement().removeContent(editorElement);
				parent.removeHandle(this);
				Main.repack();
				Main.save();
			}
		}
	}

}

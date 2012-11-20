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

@SuppressWarnings("serial")
public class EditorHandle extends JPanel implements ActionListener {
	private Element editorElement;
	private PermissionsPane parent;
	private JButton deleteButton;

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

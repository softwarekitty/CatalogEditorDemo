package gui;

import gui.widget.EditorHandle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom2.Element;

@SuppressWarnings("serial")
public class PermissionsPane extends JPanel implements ActionListener {
	private Element editorsElement;
	private JPanel container;
	private JTextField field;
	private JButton addButton;

	public PermissionsPane(Element editorsElement) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(getAdditionPane());

		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		this.editorsElement = editorsElement;
		List<Element> editors = editorsElement.getChildren("EDITOR");
		for (Element editor : editors) {
			container.add(new EditorHandle(editor, this));
		}
		add(container);
	}

	private JPanel getAdditionPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(new JLabel("Add An Editor with NetID "));
		field = new JTextField(22);
		panel.add(field);
		addButton = new JButton("+");
		addButton.addActionListener(this);
		panel.add(addButton);
		return panel;
	}
	
	public void removeHandle(EditorHandle toRemove){
		container.remove(toRemove);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == addButton) {
			String editorNetID = field.getText();
			if (Main.debug2) {
				System.out.println(" button pressed with field text: "
						+ editorNetID);
			}
			if (editorNetID != null && !editorNetID.equals("")) {
				Element newEditor = new Element("EDITOR");
				newEditor.setAttribute("netID", editorNetID);
				editorsElement.addContent(newEditor);
				container.add(new EditorHandle(newEditor,this));
				Main.repack();
				Main.save();
			}
		}

	}

}

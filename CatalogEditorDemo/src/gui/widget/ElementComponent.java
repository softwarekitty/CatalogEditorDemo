package gui.widget;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdom2.Element;

import facade.Syncable;

@SuppressWarnings("serial")
public class ElementComponent extends JPanel implements Syncable{
	private JTextField field;
	private Element element;
	protected boolean hasUnsavedChanges;
	private SaveButton saveButton;
	
	public ElementComponent(Element element, SaveButton saveButton){
		this.element=element;
		hasUnsavedChanges = false;
		this.saveButton = saveButton;
		saveButton.addToList(this);

		field = new JTextField(element.getText());
		field.getDocument().addDocumentListener(new FieldListener());
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(new JLabel(element.getName().toLowerCase()));
		add(field);
		
	}

	@Override
	public void sync() {
		element.setText(field.getText());
		handleChange();		
	}

	@Override
	public boolean needsSyncing() {
		return hasUnsavedChanges;
	}
	
	private void handleChange() {
		if (field.getText().equals(element.getText())) {
			hasUnsavedChanges = false;
			saveButton.checkAllChanges();
		} else {
			hasUnsavedChanges = true;
			saveButton.setEnabled(true);
		}
		if (hasUnsavedChanges) {
			setBackground(Color.RED);
		} else {
			setBackground(Color.LIGHT_GRAY);
		}
	}
	
	class FieldListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// not used
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			handleChange();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			handleChange();
		}

	}

}

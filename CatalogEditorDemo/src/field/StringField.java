package field;


import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import undecided.Util;
import facade.StringFacade;
import facade.StringListener;


@SuppressWarnings("serial")
public class StringField extends JPanel implements StringListener {
	private StringFacade facade;
	private JEditorPane edit;

	public StringField(StringFacade facade) {
		this.facade = facade;

		edit = new JEditorPane();
		edit.setText(facade.getS());
		edit.setPreferredSize(new Dimension(500,30));
		edit.getDocument().addDocumentListener(new FieldListener());
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(new JLabel(facade.getName().toLowerCase()));
		add(new JScrollPane(edit));

	}

	@Override
	public void react(boolean hasUnsavedChanges, String s) {
		if(!edit.getText().equals(s)){
			edit.setText(s);
		}
		if (hasUnsavedChanges) {
			setBackground(Util.colorBook.getBackground(facade.getName()));
		} else {
			setBackground(UIManager.getColor ( "Panel.background" ));
		}
	}
	
	@Override
	public String toString(){
		return "StringField::"+facade.getName();
	}

	class FieldListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// not used
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			if(!edit.getText().equals(facade.getS())){
				facade.setS(edit.getText());
			}
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			if(!edit.getText().equals(facade.getS())){
				facade.setS(edit.getText());
			}
		}

	}

	@Override
	public String getUniqueName() {
		return this.toString();
	}

}

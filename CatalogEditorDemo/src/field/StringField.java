package field;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;

import org.jdom2.Element;

import undecided.Util;
import facade.AbstractFacade;
import facade.StringListener;
import gui.Main;

@SuppressWarnings("serial")
public class StringField extends JPanel implements StringListener {
	protected AbstractFacade facade;
	protected JTextPane edit;
	protected JLabel fieldLabel;

	public StringField(AbstractFacade facade,List<Element> editors, Dimension d){
		this(facade,editors);
		setPreferredSize(d);
	}
	
	public StringField(AbstractFacade facade,List<Element> editors) {
		this.facade = facade;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		edit = new JTextPane();
		edit.setText(facade.getS());
		edit.getDocument().addDocumentListener(new FieldListener());
		((DefaultStyledDocument) edit.getDocument())
				.setDocumentFilter(new FieldDocumentFilter());

		fieldLabel = new JLabel(facade.getName().toLowerCase());
		fieldLabel.setForeground(Util.colorBook.getBackground(facade.getID()));
		fieldLabel.setPreferredSize(new Dimension(71, 40));
		
		add(fieldLabel);
		add(new JScrollPane(edit));
		if (!Util.editingIsAllowed(editors)) {
			edit.setEditable(false);
		}
		facade.addStringListener(this);
	}

	@Override
	public void react(boolean hasUnsavedChanges, String s) {
		if(Main.debug3){
			System.out.println("in StringField.react with s: "+s + " and text: "+edit.getText());
		}
		if (!edit.getText().equals(s)) {
			edit.setText(s);
		}
		if (hasUnsavedChanges) {
			fieldLabel.setForeground(Color.black);
			setBackground(Util.colorBook.getBackground(facade.getID()));
		} else {
			fieldLabel.setForeground(Util.colorBook.getBackground(facade.getID()));
			setBackground(UIManager.getColor("Panel.background"));
		}
	}

	@Override
	public String toString() {
		return "StringField::" + facade.getName();
	}

	class FieldListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// not used
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			if (!edit.getText().equals(facade.getS())) {
				facade.setS(edit.getText());
			}
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			if (!edit.getText().equals(facade.getS())) {
				facade.setS(edit.getText());
			}
		}

	}

	@Override
	public int getType() {
		return StringListener.FIELD;
	}
	
}

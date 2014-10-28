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


/**
 * The Class StringField uses a JTextPane to map an xml field to the GUI.
 */
@SuppressWarnings("serial")
public class StringField extends JPanel implements StringListener {
	
	/** The facade. */
	protected AbstractFacade facade;
	
	/** The editable text object, here a JTextPane. */
	protected JTextPane edit;
	
	/** The field label. */
	protected JLabel fieldLabel;

	/**
	 * Instantiates a new string field, specifying its preferred size.
	 *
	 * @param facade the facade
	 * @param editors the editors
	 * @param d the d
	 */
	public StringField(AbstractFacade facade,List<Element> editors, Dimension d,boolean filterInput){
		this(facade,editors,filterInput);
		setPreferredSize(d);
	}
	
	/**
	 * Instantiates a new string field with minimal arguments.
	 *
	 * @param facade the facade
	 * @param editors the editors
	 */
	public StringField(AbstractFacade facade, List<Element> editors){
		this(facade,editors,true);
	}
	
	
	/**
	 * Instantiates a new string field, with the option to not filter input (for the HeaderEditorPane).
	 *
	 * @param facade the facade
	 * @param editors the editors
	 * @param filter input or don't
	 */
	public StringField(AbstractFacade facade,List<Element> editors, boolean filterInput) {
		this.facade = facade;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		edit = new JTextPane();
		edit.setText(facade.getS());
		edit.getDocument().addDocumentListener(new FieldListener());
		if(filterInput){
			((DefaultStyledDocument) edit.getDocument())
			.setDocumentFilter(new FieldDocumentFilter());
		}

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

	/* (non-Javadoc)
	 * @see facade.StringListener#react(boolean, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {
		return "StringField::" + facade.getName();
	}

	/**
	 * Always sets the facade to the current text value.
	 *
	 * @see FieldEvent
	 */
	class FieldListener implements DocumentListener {

		/* (non-Javadoc)
		 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
		 */
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// not used
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
		 */
		@Override
		public void insertUpdate(DocumentEvent arg0) {
			if (!edit.getText().equals(facade.getS())) {
				facade.setS(edit.getText());
			}
		}

		/* (non-Javadoc)
		 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
		 */
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			if (!edit.getText().equals(facade.getS())) {
				facade.setS(edit.getText());
			}
		}

	}

	/* (non-Javadoc)
	 * @see facade.StringListener#getType()
	 */
	@Override
	public int getType() {
		return StringListener.FIELD;
	}
	
}

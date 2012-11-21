package gui.widget;

import gui.Main;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdom2.Element;

import undecided.Util;


/**
 * The Class CredentialsDialog pops up and asks for username and password.
 */
//TODO - link with existing netID validation software
@SuppressWarnings("serial")
public class CredentialsDialog extends JDialog implements ActionListener {
	
	/** The username field. */
	private LabeledField usernameField;
	
	/** The password field. */
	private LabeledField passwordField;
	
	/** The validate button. */
	private JButton validateButton;

	/**
	 * Instantiates a new credentials dialog.
	 *
	 * @param parentFrame the parent frame
	 */
	public CredentialsDialog(JFrame parentFrame) {
		super(parentFrame, Dialog.ModalityType.DOCUMENT_MODAL);
		getContentPane().add(createGUI());
		pack();
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}

	/**
	 * Creates the gui.
	 *
	 * @return the j panel
	 */
	private JPanel createGUI() {
		// 'panel' holds the tablePanel above the plusButton and okButton
		JPanel panel = new JPanel();
		BoxLayout panelLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(panelLayout);

		// create field panel
		JPanel fieldPanel = new JPanel();
		BoxLayout fieldLayout = new BoxLayout(fieldPanel, BoxLayout.Y_AXIS);
		fieldPanel.setLayout(fieldLayout);
		usernameField = new LabeledField("username", 20);
		passwordField = new LabeledField("password", 20);
		fieldPanel.add(usernameField);
		fieldPanel.add(passwordField);

		// add the fieldPanel and button to the main panel and return it
		panel.add(fieldPanel);
		validateButton = new JButton("Validate");
		validateButton.addActionListener(this);
		panel.add(validateButton);
		return panel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// validate that the userID is in this document
		String enteredUsername = usernameField.getField().getText().trim();
		if (Main.debug2) {
			System.out.println("the username entered was: " + enteredUsername);
		}
		Element editor = Util.getElement("//EDITOR[@netID=\"" + enteredUsername
				+ "\"]");
		if (editor == null) {
			System.err.println("the editor with username " + enteredUsername
					+ " was not found");
		} else {
			if (Main.debug2) {
				System.out.println("editor " + enteredUsername + " found");
			}
			Main.setEditor(editor);
		}
		// TODO link to a system that can validate passwords for valid userIDs

		dispose();
	}

}

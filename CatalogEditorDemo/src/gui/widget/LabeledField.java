package gui.widget;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * The Class LabeledField this widget just puts a JLabel in front of a JTextField (horizontally).
 */
@SuppressWarnings("serial")
public class LabeledField extends JPanel {
	
	/** The field. */
	private JTextField field;

	/**
	 * Instantiates a new labeled field.
	 *
	 * @param name the name
	 * @param text the text
	 */
	public LabeledField(String name, String text) {
		this(name, new JTextField(text));
	}

	/**
	 * Instantiates a new labeled field.
	 *
	 * @param name the name
	 * @param size the size
	 */
	public LabeledField(String name, int size) {
		this(name, new JTextField(size));
	}

	/**
	 * Instantiates a new labeled field.
	 *
	 * @param name the name
	 * @param initialField the initial field
	 */
	public LabeledField(String name, JTextField initialField) {
		super();
		field = initialField;
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		add(new JLabel(name));
		add(field);
	}

	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public JTextField getField() {
		return field;
	}

}

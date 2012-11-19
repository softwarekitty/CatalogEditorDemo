package gui.widget;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LabeledField extends JPanel {
	private JTextField field;

	public LabeledField(String name, String text) {
		this(name, new JTextField(text));
	}

	public LabeledField(String name, int size) {
		this(name, new JTextField(size));
	}

	public LabeledField(String name, JTextField initialField) {
		super();
		field = initialField;
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		add(new JLabel(name));
		add(field);
	}

	public JTextField getField() {
		return field;
	}

}

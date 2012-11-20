package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;

import org.jdom2.Element;

import block.Range;
import block.VersionDocument;
import block.VersionFilter;

import undecided.Util;

@SuppressWarnings("serial")
public class CatalogSettingsPane extends JPanel implements ActionListener,
		DocumentListener {
	private Element catalogElement;
	private JLabel currentYear;
	private JTextArea text;
	private JButton changeButton;

	public CatalogSettingsPane(Element e) {
		this.catalogElement = e;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		currentYear = new JLabel("The current Year is "
				+ Main.getDocument().getRootElement()
						.getAttributeValue("currentYear"));
		currentYear.setAlignmentX(CENTER_ALIGNMENT);
		add(currentYear);

		JLabel setYear = new JLabel("set year to: ");
		setYear.setAlignmentX(CENTER_ALIGNMENT);
		add(setYear);
		text = new JTextArea();
		text.setMinimumSize(new Dimension(200, 25));
		text.setMaximumSize(new Dimension(200, 25));
		text.setDocument(new DefaultStyledDocument());
		text.getDocument().addDocumentListener(this);
		((DefaultStyledDocument) text.getDocument())
				.setDocumentFilter(new YearFilter());
		text.setAlignmentX(CENTER_ALIGNMENT);
		add(text);
		changeButton = new JButton("change year");
		changeButton.addActionListener(this);
		changeButton.setAlignmentX(CENTER_ALIGNMENT);
		changeButton.setEnabled(false);
		add(changeButton);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == changeButton) {
			int reply = JOptionPane.showConfirmDialog(null,
					"Really Change Year To " + text.getText() + "?",
					"Confirm Change", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				LinkedList<Element> courses = Util.getElements("//COURSE");
				ArrayList<Element> versionsToReplace = new ArrayList<Element>();
				for (Element course : courses) {
					List<Element> children = course.getChildren("VERSION");
					for (Element child : children) {
						if (child.getAttributeValue("catalogYear").equals(
								text.getText())) {
							versionsToReplace.add(child);
						}
					}
				}
				if (versionsToReplace.size() > 0) {
					int response = JOptionPane
							.showConfirmDialog(
									null,
									"Changing the year will replace "
											+ versionsToReplace.size()
											+ "\n existing versions with default versions.  \nSet year anyway?",
									"Confirm Version Deletion",
									JOptionPane.YES_NO_OPTION);
					if (response == JOptionPane.YES_OPTION) {
						setCatalogYear(courses);
						for (Element oldVersion : versionsToReplace) {
							oldVersion.detach();
						}
					}
				} else {
					setCatalogYear(courses);
				}
			}
		}
	}

	private void setCatalogYear(LinkedList<Element> courses) {
		catalogElement.setAttribute("currentYear", text.getText());
		for (Element course : courses) {
			course.addContent(Util.getDefaultVersion());
		}
		Main.repack();
		Main.save();
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// do nothing
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		handleChange();

	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		handleChange();
	}

	private void handleChange() {
		if (text.getText().length() == 0) {
			changeButton.setEnabled(false);
		} else {
			changeButton.setEnabled(true);
		}
	}

	class YearFilter extends DocumentFilter {
		public void insertString(DocumentFilter.FilterBypass fb, int offset,
				String string, AttributeSet attr) throws BadLocationException {
			if (isInt(string)) {
				super.insertString(fb, offset, string, attr);
			}
		}

		public void replace(DocumentFilter.FilterBypass fb, int offset,
				int length, String string, AttributeSet attr)
				throws BadLocationException {
			boolean isInt = isInt(string);
			if (length > 0 && isInt) {
				fb.remove(offset, length);
			}
			if (isInt) {
				insertString(fb, offset, string, attr);
			}
		}

		public boolean isInt(String s) {
			try {
				Integer.parseInt(s);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}
}

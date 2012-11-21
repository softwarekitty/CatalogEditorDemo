package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

import undecided.Util;
import gui.Main;

/**
 * The Class CatalogSettingsPane allows the user to set the current year of the
 * project. Anticipated use is for when time passes and the Catalog database is
 * out of date. Currently only makes a default version for each course. 
 */
//TODO - Create new versions based on the previous year's versions.
@SuppressWarnings("serial")
public class CatalogSettingsPane extends JPanel implements ActionListener,
		DocumentListener {

	/** The catalog element. */
	private Element catalogElement;

	/** The current year. */
	private JLabel currentYear;

	/** The text. */
	private JTextArea text;

	/** The change button. */
	private JButton changeButton;

	/**
	 * Instantiates a new catalog settings pane.
	 * 
	 * @param e
	 *            the CATALOG element
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == changeButton) {
			int reply = JOptionPane
					.showConfirmDialog(
							null,
							"Really Change Year To "
									+ text.getText()
									+ "?\nThis will create default versions\nof all courses for that year.",
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
									"There are already "
											+ versionsToReplace.size()
											+ " existing \nversions for the year "
											+ text.getText()
											+ ".  \nDo you want to replace them?\nSelecting no will just\nchange the catalog year\nwithout making default versions.",
									"Deletion Existing Versions?",
									JOptionPane.YES_NO_OPTION);
					if (response == JOptionPane.YES_OPTION) {
						for (Element oldVersion : versionsToReplace) {
							oldVersion.getParentElement().removeContent(
									oldVersion);
						}
						setCatalogYear(courses);
					} else {
						catalogElement.setAttribute("currentYear",
								text.getText());
						Main.repack();
						Main.save();
					}
				} else {
					setCatalogYear(courses);
				}
			}
		}
	}

	/**
	 * Sets the catalog year.
	 * 
	 * @param courses
	 *            the new catalog year
	 */
	private void setCatalogYear(LinkedList<Element> courses) {
		catalogElement.setAttribute("currentYear", text.getText());
		for (Element course : courses) {
			course.addContent(Util.getDefaultVersion());
		}
		Main.repack();
		Main.save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
	 * DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.
	 * DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent arg0) {
		handleChange();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
	 * DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent arg0) {
		handleChange();
	}

	/**
	 * Handle change.
	 */
	private void handleChange() {
		if (text.getText().length() == 0) {
			changeButton.setEnabled(false);
		} else {
			changeButton.setEnabled(true);
		}
	}

	/**
	 * The Class YearFilter allows only entering integer input.
	 */
	class YearFilter extends DocumentFilter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.text.DocumentFilter#insertString(javax.swing.text.
		 * DocumentFilter.FilterBypass, int, java.lang.String,
		 * javax.swing.text.AttributeSet)
		 */
		public void insertString(DocumentFilter.FilterBypass fb, int offset,
				String string, AttributeSet attr) throws BadLocationException {
			if (isInt(string)) {
				super.insertString(fb, offset, string, attr);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter
		 * .FilterBypass, int, int, java.lang.String,
		 * javax.swing.text.AttributeSet)
		 */
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

		/**
		 * Checks if is int.
		 * 
		 * @param s
		 *            the string to check
		 * @return true, if is int
		 */
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

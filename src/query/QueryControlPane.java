package query;

import gui.Main;
import gui.widget.AbstractHandle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import undecided.Util;

/**
 * The Class QueryControlPane helps the user create and manage their queries,
 * investigate global and other saved queries, and view the results of any
 * query.
 */
//TODO - clear out the query text fields after saving one
@SuppressWarnings("serial")
public class QueryControlPane extends JPanel implements ActionListener,
		Handleable, ItemListener, ChangeListener, Displayable {

	/** The results panel. */
	private JPanel resultsPanel;

	/** The t pane. */
	private JTabbedPane tPane;

	/** The display. */
	private DisplayDialog display;

	/** The description field. */
	private JTextField descriptionField;

	/** The query field. */
	private JTextField queryField;

	/** The add button. */
	private JButton addButton;

	/** The run button. */
	private JButton runButton;

	/** The display in box. */
	private JCheckBox displayInBox;

	/**
	 * Instantiates a new query control pane.
	 * 
	 * @param resultsPanel
	 *            the results panel
	 * @param lastTabUsed
	 *            the last tab used
	 */
	public QueryControlPane(JPanel resultsPanel, String lastTabUsed) {
		this.resultsPanel = resultsPanel;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		tPane = new JTabbedPane();
		tPane.addChangeListener(this);
		tPane.add("new", getNewPane());
		tPane.add("global", getGlobalPane());
		tPane.add("mine", getMinePane());
		tPane.add("all", getAllPane());
		for (int i = 0; i < tPane.getTabCount(); i++) {
			if (tPane.getTitleAt(i).equals(lastTabUsed)) {
				tPane.setSelectedIndex(i);
				break;
			}
		}
		add(tPane, BorderLayout.CENTER);
	}

	/**
	 * Gets the new pane.
	 * 
	 * @return the new pane
	 */
	private JPanel getNewPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel write = new JLabel("write an XPath query: ");
		write.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(write);
		queryField = new JTextField(50);
		panel.add(queryField);
		JLabel describe = new JLabel("brief description: ");
		describe.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(describe);
		descriptionField = new JTextField(50);
		panel.add(descriptionField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		JLabel test = new JLabel("test the current query");
		buttonPanel.add(test);

		runButton = new JButton(new ImageIcon("play_icon.jpg"));
		runButton.addActionListener(this);
		buttonPanel.add(runButton);

		buttonPanel.add(Box.createHorizontalGlue());

		displayInBox = new JCheckBox("display in separate window");
		displayInBox.setSelected(displayInDialog());
		displayInBox.addItemListener(this);
		buttonPanel.add(displayInBox);

		buttonPanel.add(Box.createHorizontalGlue());

		addButton = new JButton("save new query to 'mine'");
		addButton.addActionListener(this);
		buttonPanel.add(addButton);

		panel.add(buttonPanel);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return panel;
	}

	/**
	 * Gets the query panel.
	 * 
	 * @param xPathExpression
	 *            the x path expression
	 * @param controlDelete
	 *            the control delete
	 * @return the query panel
	 */
	private ContainerPanel getQueryPanel(String xPathExpression,
			boolean controlDelete) {
		ContainerPanel panel = new ContainerPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		//
		// JPanel headerPanel = new JPanel();
		// headerPanel.add(new JLabel("hey, hye looka t me"));
		// panel.add(headerPanel);

		LinkedList<Element> globalQueries = Util.getElements(xPathExpression);
		JPanel container = panel.getContainerPanel();
		for (Element e : globalQueries) {
			container.add(new QueryHandle(e, this, false, controlDelete));
		}
		JScrollPane scrollPane = new JScrollPane(container);
		scrollPane.setPreferredSize(new Dimension(800, 135));
		panel.add(scrollPane);
		return panel;
	}

	/**
	 * Gets the global pane.
	 * 
	 * @return the global pane
	 */
	private ContainerPanel getGlobalPane() {
		return getQueryPanel("//QUERY[@global=\"true\"]", false);
	}

	/**
	 * Gets the mine pane.
	 * 
	 * @return the mine pane
	 */
	private ContainerPanel getMinePane() {
		return getQueryPanel("//QUERY[@author=\""
				+ Main.getEditor().getAttributeValue("netID") + "\"]", true);
	}

	/**
	 * Gets the all pane.
	 * 
	 * @return the all pane
	 */
	private ContainerPanel getAllPane() {
		return getQueryPanel("//QUERY", false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see query.Handleable#removeHandle(gui.widget.AbstractHandle)
	 */
	@Override
	public void removeHandle(AbstractHandle toRemove) {
		((ContainerPanel) tPane.getSelectedComponent()).getContainerPanel()
				.remove(toRemove);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see query.Displayable#display(java.lang.String)
	 */
	@Override
	public void display(String xPathExpression) {
		LinkedList<Element> elements = Util.getElements(xPathExpression);
		Element root = new Element("RESULTS").setAttribute("query",
				xPathExpression);
		for (Element e : elements) {
			root.addContent(e.clone());
		}
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		String toDisplay = out.outputString(root);
		JTextPane text = new JTextPane();
		text.setText(toDisplay);
		text.setEditable(false);
		resultsPanel.add(new JScrollPane(text));
		Main.repack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see query.Displayable#displayInDialog()
	 */
	@Override
	public boolean displayInDialog() {
		Element editor = Main.getEditor();
		if (editor == null) {
			System.err
					.println("null editor in QueryControlPane.displayInDialog()");
			return false;
		}
		String displayInDialog = editor.getAttributeValue("displayInDialog");
		if (displayInDialog == null) {
			displayInDialog = "false";
			Main.setEditorSetting("displayInDialog", displayInDialog);
		}
		return Boolean.parseBoolean(displayInDialog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see query.Displayable#getDisplay()
	 */
	@Override
	public DisplayDialog getDisplay() {
		return display;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see query.Displayable#setDisplay(query.DisplayDialog)
	 */
	@Override
	public void setDisplay(DisplayDialog display) {
		this.display = display;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == runButton) {
			if (descriptionField.getText().length() == 0
					|| queryField.getText().length() == 0) {
				// do nothing
			} else {
				if (displayInDialog()) {
					if (display != null) {
						display.dispose();
					}
					setDisplay(new DisplayDialog(Main.getFrame(),
							descriptionField.getText(), queryField.getText()));
				} else {
					LinkedList<Element> elements = Util.getElements(queryField
							.getText());
					Element root = new Element("RESULTS").setAttribute("query",
							queryField.getText());
					for (Element result : elements) {
						root.addContent(result.clone());
					}
					XMLOutputter out = new XMLOutputter(
							Format.getPrettyFormat());
					String toDisplay = out.outputString(root);
					JTextPane text = new JTextPane();
					text.setText(toDisplay);
					text.setEditable(false);
					resultsPanel.removeAll();
					resultsPanel.add(new JScrollPane(text));
					Main.repack();
				}
			}
		} else if (e.getSource() == addButton) {
			String description = descriptionField.getText();
			String query = queryField.getText();
			Element editor = Main.getEditor();
			String netID = null;
			if (editor != null) {
				netID = editor.getAttributeValue("netID");
			}
			if (description != null && !description.equals("") && query != null
					&& !query.equals("") && netID != null && !netID.equals("")) {
				Element newQuery = new Element("QUERY");
				newQuery.setAttribute("description", description);
				newQuery.setText(query);
				newQuery.setAttribute("author", netID);
				newQuery.setAttribute("global", "false");
				String queriesXPath = "CATALOG/QUERIES";
				Element queriesElement = Util.getElement(queriesXPath);
				queriesElement.addContent(newQuery);
				tPane.setComponentAt(2, getMinePane());
				Main.repack();
				Main.save();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == displayInBox) {
			Element editor = Main.getEditor();
			if (editor == null) {
				System.err
						.println("null editor in QueryControlPane.displayInDialog()");
				return;
			} else {
				String displayInDialog = Boolean.valueOf(
						displayInBox.isSelected()).toString();
				Main.setEditorSetting("displayInDialog", displayInDialog);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
	 * )
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		Main.setEditorSetting("tab", tPane.getTitleAt(tPane.getSelectedIndex()));
	}
}

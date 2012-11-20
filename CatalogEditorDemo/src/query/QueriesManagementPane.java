package query;

import gui.CourseEditorPane;
import gui.Main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom2.Element;


@SuppressWarnings("serial")
public class QueriesManagementPane extends JPanel implements ActionListener,Handleable {
	private Element queriesElement;
	private JPanel container;
	private JTextField descriptionField;
	private JTextField queryField;
	private JButton addButton;
	private DisplayDialog display;

	public QueriesManagementPane(Element queriesElement) {
		this.queriesElement = queriesElement;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(getNewQueryPane());
		List<Element> queries = queriesElement.getChildren("QUERY");
		for (Element query : queries) {
			if (Main.debug2) {
				System.out.println("adding query "
						+ query.getAttributeValue("description")
						+ " to queries pane");
			}
			container.add(new QueryHandle(query, this));
		}
		//container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(container);
		setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 200));
	}

	private JPanel getNewQueryPane() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("write an XPath query: "));
		queryField = new JTextField(50);
		panel.add(queryField);
		panel.add(new JLabel("brief description: "));
		descriptionField = new JTextField(50);
		panel.add(descriptionField);

		addButton = new JButton("+");
		addButton.addActionListener(this);
		panel.add(addButton);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == addButton) {
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
				queriesElement.addContent(newQuery);
				container.add(new QueryHandle(newQuery, this));
				Main.repack();
				Main.save();
			}
		}

	}

	public void removeHandle(QueryHandle toRemove) {
		container.remove(toRemove);
	}
	
	public DisplayDialog getDisplay(){
		return display;
	}
	
	public boolean displayInDialog(){
		return true;
	}
	
	public void display(String xmlExpression){
		//do nothing
	}

	@Override
	public void setDisplay(DisplayDialog display) {
		this.display=display;
	}

}

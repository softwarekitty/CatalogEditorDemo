package gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jdom2.Element;


/**
 * The Class StaticViewPane displays the Static Document.
 */
@SuppressWarnings("serial")
public class StaticViewPane extends JPanel{
	
	/**
	 * Instantiates a new static view pane.
	 *
	 * @param element the element
	 */
	public StaticViewPane(Element element){
		StaticDocument doc = new StaticDocument(element);
		JTextPane text = new JTextPane();
		text.setDocument(doc);
		text.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setPreferredSize(new Dimension(700, 700));
		add(scrollPane);
	}
}

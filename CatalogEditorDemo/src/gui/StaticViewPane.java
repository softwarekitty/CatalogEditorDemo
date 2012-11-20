package gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jdom2.Element;

import undecided.Util;

@SuppressWarnings("serial")
public class StaticViewPane extends JPanel{
	
	public StaticViewPane(Element element){
		StaticDocument doc = new StaticDocument(element);
		JTextPane text = new JTextPane();
		text.setDocument(doc);
		text.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setPreferredSize(new Dimension(700, 700));
		add(scrollPane);
		
		
		
		
		
		
		
		
		
		
		
		
//		String toDisplay =  Util.getStringFromElement(programElement);
//		toDisplay = toDisplay==null?"nothing to display":toDisplay;
//		JTextPane text = new JTextPane();
//		text.setText(toDisplay);
//		text.setEditable(false);
//		JScrollPane scrollPane =new JScrollPane(text);
//		scrollPane.setPreferredSize(new Dimension(700, 700));
//		add(scrollPane);
	}
}

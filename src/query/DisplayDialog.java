package query;

import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import undecided.Util;


/**
 * The Class DisplayDialog pops up and displays an xquery result.
 */
@SuppressWarnings("serial")
public class DisplayDialog extends JDialog {

	/**
	 * Instantiates a new display dialog.
	 *
	 * @param frame the frame
	 * @param title the title
	 * @param xPathExpression the x path expression
	 */
	public DisplayDialog(JFrame frame, String title, String xPathExpression) {
		super(frame, title, false);
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
		this.getContentPane().add(new JScrollPane(text));
		this.pack();
		this.setVisible(true);

	}

}

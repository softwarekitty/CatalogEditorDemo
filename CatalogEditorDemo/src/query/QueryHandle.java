package query;

import gui.Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import org.jdom2.Element;

@SuppressWarnings("serial")
public class QueryHandle extends JPanel implements ActionListener {
	private Element queryElement;
	private Handleable parent;
	private JPanel container;
	private JButton deleteButton;
	private JButton globalButton;
	private JButton playButton;
	private boolean isGlobal;
	private boolean controlGlobal;
	private boolean controlDelete;

	public QueryHandle(Element queryElement, Handleable parent) {
		this(queryElement, parent, true, true);
	}

	public QueryHandle(Element queryElement, Handleable parent,
			boolean controlGlobal, boolean controlDelete) {
		this.parent = parent;
		this.queryElement = queryElement;
		this.controlGlobal = controlGlobal;
		this.controlDelete = controlDelete;

		isGlobal = Boolean.parseBoolean(queryElement
				.getAttributeValue("global"));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		playButton = new JButton(new ImageIcon("play_icon.jpg"));
		playButton.addActionListener(this);
		playButton.setBorder(BorderFactory.createEmptyBorder());
		playButton.setContentAreaFilled(false);
		container.add(playButton);

		JTextArea text = new JTextArea(queryElement.getText());
		text.setEditable(false);
		text.setToolTipText(queryElement.getAttributeValue("description"));
		container.add(text);

		if(controlGlobal){
			globalButton = new JButton(new ImageIcon("icon-globe.jpg"));
			globalButton.addActionListener(this);
			globalButton.setBorder(BorderFactory.createEmptyBorder());
			globalButton.setContentAreaFilled(false);
			container.add(globalButton);
		}

		if(controlDelete){
			deleteButton = new JButton(new ImageIcon("icon_x.gif"));
			deleteButton.addActionListener(this);
			deleteButton.setBorder(BorderFactory.createEmptyBorder());
			deleteButton.setContentAreaFilled(false);
			container.add(deleteButton);
		}


		container.setBackground(Color.WHITE);
		container.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, getBorderColor()));
		add(container);
		setMaximumSize(new Dimension(Integer.MAX_VALUE,25));
	}

	private Color getBorderColor() {
		return isGlobal && controlGlobal ? Color.GREEN : UIManager
				.getColor("Panel.background");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == deleteButton && controlDelete) {
			int reply = JOptionPane.showConfirmDialog(null, "Confirm Delete",
					"Really Delete Query?", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				queryElement.getParentElement().removeContent(queryElement);
				parent.removeHandle(this);
				Main.repack();
				Main.save();
			}
		} else if (event.getSource() == globalButton && controlGlobal) {
			String message = isGlobal ? "Really Remove Global Status?"
					: "Really Add Global Status?";
			int reply = JOptionPane.showConfirmDialog(null, message,
					"Set Global", JOptionPane.YES_NO_OPTION);
			if (reply == JOptionPane.YES_OPTION) {
				isGlobal = !isGlobal;
				queryElement.setAttribute("global", Boolean.valueOf(isGlobal)
						.toString());
				setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2,
						getBorderColor()));
				Main.repack();
				Main.save();
			}
		} else if (event.getSource() == playButton) {
			if (parent.displayInDialog()) {
				DisplayDialog display = parent.getDisplay();
				if (display != null) {
					display.dispose();
				}
				parent.setDisplay(new DisplayDialog(Main.getFrame(),
						queryElement.getAttributeValue("description"),
						queryElement.getText()));
			} else {
				parent.display(queryElement.getText());
			}

		}
	}
}

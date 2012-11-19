package comments;

import facade.Syncable;
import gui.CourseEditorPane;
import gui.Main;
import gui.widget.SaveButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdom2.Element;

@SuppressWarnings("serial")
public class CommentsPanel extends JPanel implements ActionListener {
	private HashMap<String, CommentPanel> commentMap;
	private JButton plusButton;
	private SaveButton saveButton;
	private Element commentsElement;
	private JPanel commentHolder;

	public CommentsPanel(Element commentsElement, SaveButton saveButton) {
		this.commentsElement = commentsElement;
		this.saveButton = saveButton;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		commentHolder = new JPanel();
		commentHolder.setLayout(new BoxLayout(commentHolder, BoxLayout.Y_AXIS));

		commentMap = new HashMap<String, CommentPanel>();
		plusButton = new JButton("+");
		plusButton.addActionListener(this);
		List<Element> list = commentsElement.getChildren("COMMENT");
		for (Element e : list) {
			CommentPanel c = new CommentPanel(e);
			commentMap.put(c.getAuthor(), c);
			commentHolder.add(c);
		}
		add(commentHolder);
		//add(new JScrollPane(commentHolder));
		add(plusButton);
		setMaximumSize(new Dimension(CourseEditorPane.WIDTH, 200));
	}

	class CommentPanel extends JPanel implements DocumentListener, Syncable {
		private String author;
		private Element commentElement;
		private JTextPane edit;
		private boolean hasUnsavedChanges;
		private JPanel container;

		public CommentPanel(Element commentElement) {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
			saveButton.addToList(this);
			this.commentElement = commentElement;
			hasUnsavedChanges = false;

			author = commentElement.getAttributeValue("netID");
			JLabel l = new JLabel("author: " + author);
			l.setBackground(Color.WHITE);
			l.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
					Color.LIGHT_GRAY));
			JLabel labelPanel = new JLabel();
			labelPanel.add(l);
			container.add(l);

			edit = new JTextPane();
			edit.setText(commentElement.getText());
			edit.getDocument().addDocumentListener(this);
			edit.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			container.add(new JScrollPane(edit));
			container.setBackground(Color.WHITE);
			container
					.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10,
					UIManager.getColor("Panel.background")));
			add(container);
			if (!Main.getEditor().getAttribute("netID").getValue()
					.equals(author)) {
				edit.setEditable(false);
			}
		}

		public void initializeNewComment() {
			edit.setText("write comment here");
			edit.selectAll();
			handleChange();
		}

		public void blink(Color c) {
			container.setBackground(c);
		}

		public String getAuthor() {
			return author;
		}

		public Element getElement() {
			return commentElement;
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
			if (edit.getText().equals(commentElement.getText())) {
				hasUnsavedChanges = false;
				saveButton.checkAllChanges();
				container.setBackground(Color.WHITE);
			} else {
				hasUnsavedChanges = true;
				saveButton.setEnabled(true);
				container.setBackground(Color.RED);
			}
		}

		@Override
		public void sync() {
			hasUnsavedChanges = false;
			commentElement.setText(edit.getText());
			container.setBackground(Color.WHITE);
		}

		@Override
		public boolean needsSyncing() {
			return hasUnsavedChanges;
		}
	}

	public void removeComment(CommentPanel cp) {
		commentHolder.remove(cp);
		saveButton.removeFromList(cp);
		commentMap.remove(cp.getAuthor());
		commentsElement.removeContent(cp.getElement());
		Main.save();
		Main.repack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == plusButton) {
			if (Main.debug3) {
				System.out.println("adding new comment");
			}
			String currentEditor = Main.getEditor().getAttribute("netID")
					.getValue();
			CommentPanel cp = commentMap.get(currentEditor);
			if (cp != null) {
				CommentBlinker blinky = new CommentBlinker(cp);
				blinky.start();
			} else {
				Element newComment = new Element("COMMENT");
				newComment.setAttribute("netID", currentEditor);
				commentsElement.addContent(newComment);
				Main.save();

				CommentPanel newPanel = new CommentPanel(newComment);
				commentMap.put(currentEditor, newPanel);
				commentHolder.add(newPanel);
				newPanel.initializeNewComment();
			}
		}
	}

	class CommentBlinker extends Thread {
		private CommentPanel panel;
		private boolean blink;

		public CommentBlinker(CommentPanel panel) {
			this.panel = panel;
		}

		public void run() {
			blink = true;
			for (int i = 0; i < 8; i++) {
				if (blink) {
					panel.blink(Color.DARK_GRAY);
				} else {
					panel.blink(Color.WHITE);
				}
				blink = !blink;
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

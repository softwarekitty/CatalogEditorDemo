package block;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;

import facade.VersionFacade;
import gui.Main;

@SuppressWarnings("serial")
public class BlockPanel extends JPanel implements DocumentListener,
		ChangeHandler {
	private static int counter = 0;
	private JEditorPane edit;
	private VersionFacade vFacade;
	private VersionDocument doc;
	private int lastCaretPosition;

	public BlockPanel(VersionFacade facade) {
		this.vFacade = facade;
		// create pane
		edit = new JEditorPane();
		edit.setPreferredSize(new Dimension(500,300));

		// associate VersionEditorKit (which assigns a VersionDocument to pane)
		edit.setEditorKit(new VersionEditorKit(this));

		// store a reference to the document
		doc = (VersionDocument) edit.getDocument();

		// create the version from the facade
		doc.insertVersion(facade);
		lastCaretPosition = edit.getCaretPosition();
		addListenerAndFilter();

		// add the pane to this panel
		add(edit);
	}

	private void addListenerAndFilter() {

		// make this the document listener for the VersionDocument
		edit.getDocument().addDocumentListener(this);

		// add a filter that protects the elements from being deleted
		((DefaultStyledDocument) edit.getDocument())
				.setDocumentFilter(new VersionFilter((VersionDocument) edit
						.getDocument()));
	}

	// remove the restrictive filter and listener to allow free modification
	public void removeListenerAndFilter() {
		edit.getDocument().removeDocumentListener(this);
		((DefaultStyledDocument) edit.getDocument())
				.setDocumentFilter(new DocumentFilter());
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// do nothing
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		handleChange(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		handleChange(e);
	}

	public void handleChange(DocumentEvent e) {
		int caretMovement = 0;
		if (e.getType() == DocumentEvent.EventType.INSERT) {
			caretMovement = e.getLength();
		} else if (e.getType() == DocumentEvent.EventType.REMOVE) {
			caretMovement = e.getLength() == 1 ? -1 : 0;
		}

		// remove this as a documentListener so that we can modify the document
		// without triggering this
		removeListenerAndFilter();

		// notify the facade of the change(s)
		doc.notifyFacade(e);

		// remember where the caret was
		lastCaretPosition = edit.getCaretPosition() + caretMovement;
		handleChange("BlockPanel");
		
	}

	public void handleChange(String name) {
		// remove everything from the document
		// then recreate the new facade
		InsertingThread insertingThread = new InsertingThread(name);
		insertingThread.start();
	}
	
	class InsertingThread extends Thread{
		String name;
		public InsertingThread(String name){
			this.name = name;
		}
		public void run() {
			try {
				SwingUtilities.invokeAndWait(new ReInsert(name));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Main.debug1) {
				System.out.println("Finished on " + Thread.currentThread());

			}
		}
	}

	class ReInsert implements Runnable {
		private String trace;

		public ReInsert(String trace) {
			this.trace = trace;
		}

		@Override
		public void run() {
			if (Main.debug1) {
				System.out.println("counter:" + counter++ + " initiated by: "
						+ trace);
			}
			try {
				removeListenerAndFilter();
				doc.remove(0, doc.getLength());
				doc.insertVersion(vFacade);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			// put the listener and filter back
			addListenerAndFilter();
			edit.setCaretPosition(Math.min(lastCaretPosition,doc.getLength()));
		}

	}
}

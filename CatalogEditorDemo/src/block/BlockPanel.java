package block;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;

import undecided.Util;
import facade.VersionFacade;
import gui.CourseEditorPane;
import gui.Main;

/**
 * The Class BlockPanel is the containing class for all the workings of the
 * block package. It creates the version document and is in charge of notifying
 * the facade of what has changed when an insert, remove or replace happens to
 * the document. A filter is also kept attached to the document to prevent
 * constant ranges from being modified and tabs and newlines from being entered.
 * It also keeps track of the caret position so that after a modification, the
 * caret seems to be in the right place.
 */
@SuppressWarnings("serial")
public class BlockPanel extends JPanel implements DocumentListener,
		ChangeHandler {

	/** The counter - used in debugging to track threads. */
	private static int counter = 0;

	/** The editor pane that holds the document. */
	private JEditorPane edit;

	/** The version facade. */
	private VersionFacade vFacade;

	/** The version document. */
	private VersionDocument doc;

	/** The last caret position. */
	private int lastCaretPosition;

	/**
	 * Instantiates a new block panel.
	 * 
	 * @param facade
	 *            the facade
	 * @param editors
	 *            the editors allowed to edit this document
	 */
	public BlockPanel(VersionFacade facade, List<org.jdom2.Element> editors) {
		this.vFacade = facade;
		setMaximumSize(new Dimension(CourseEditorPane.WIDTH, Integer.MAX_VALUE));

		// create pane
		edit = new JEditorPane();

		// associate VersionEditorKit (which assigns a VersionDocument to pane)
		edit.setEditorKit(new VersionEditorKit(this, true));

		// store a reference to the document
		doc = (VersionDocument) edit.getDocument();

		// create the version from the facade
		doc.insertVersion(facade);
		lastCaretPosition = edit.getCaretPosition();
		addListenerAndFilter();

		// add the pane to this panel
		JScrollPane container = new JScrollPane(edit);
		add(container);
		container.setPreferredSize(new Dimension(CourseEditorPane.WIDTH, 140));

		if (!Util.editingIsAllowed(editors)) {
			edit.setEditable(false);
		}
	}

	/**
	 * Adds the listener and filter.
	 */
	private void addListenerAndFilter() {

		// make this the document listener for the VersionDocument
		edit.getDocument().addDocumentListener(this);

		// add a filter that protects the elements from being deleted
		((DefaultStyledDocument) edit.getDocument())
				.setDocumentFilter(new VersionFilter((VersionDocument) edit
						.getDocument()));
	}

	// remove the restrictive filter and listener to allow free modification
	/*
	 * (non-Javadoc)
	 * 
	 * @see block.ChangeHandler#removeListenerAndFilter()
	 */
	public void removeListenerAndFilter() {
		edit.getDocument().removeDocumentListener(this);
		((DefaultStyledDocument) edit.getDocument())
				.setDocumentFilter(new DocumentFilter());
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return edit.getText();
	}

	/**
	 * Gets the doc.
	 * 
	 * @return the doc
	 */
	public VersionDocument getDoc() {
		return (VersionDocument) edit.getDocument();
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
	public void insertUpdate(DocumentEvent e) {
		handleChange(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
	 * DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		handleChange(e);
	}

	/**
	 * Handle any change to the document by removing the listener and filter,
	 * figuring out the lastCaretPosition and then calling a helper method that
	 * starts up a new inserting thread.
	 * 
	 * @param e
	 *            the DocumentEvent
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see block.ChangeHandler#handleChange(java.lang.String)
	 */
	public void handleChange(String name) {
		// remove everything from the document
		// then recreate the new facade
		InsertingThread insertingThread = new InsertingThread(name);
		insertingThread.start();
	}

	/**
	 * The Class InsertingThread.
	 */
	class InsertingThread extends Thread {

		/** The name of the facade responsible for this thread's creation. */
		String name;

		/**
		 * Instantiates a new inserting thread.
		 * 
		 * @param name
		 *            the name of the facade responsible for this thread's
		 *            creation
		 */
		public InsertingThread(String name) {
			this.name = name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			try {
				SwingUtilities.invokeAndWait(new ReInsert(name));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (Main.debug1) {
				System.out.println("Finished Inserting Thread initiated by "
						+ name + " on " + Thread.currentThread());

			}
		}
	}

	/**
	 * The Class ReInsert.
	 */
	class ReInsert implements Runnable {

		/** The name of the facade responsible for this thread's creation. */
		private String trace;

		/**
		 * Instantiates a new re insert.
		 * 
		 * @param trace
		 *            the name of the facade responsible for this thread's
		 *            creation
		 */
		public ReInsert(String trace) {
			this.trace = trace;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
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
			// put the listener, filter and caret back
			addListenerAndFilter();
			edit.setCaretPosition(Math.min(lastCaretPosition, doc.getLength()));
		}

	}
}

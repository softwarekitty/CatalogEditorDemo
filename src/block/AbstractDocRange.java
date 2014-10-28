package block;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;

import facade.AbstractFacade;
import facade.StringListener;
import gui.Main;

/**
 * The Class AbstractDocRange is a base for modifyable doc ranges. It has the
 * critical function 'resetEverything' which removes all the listeners and then
 * reinserts a document. It also has some convenience methods like getID and
 * getFacade.
 */
public abstract class AbstractDocRange extends ConstantRange implements
		StringListener {

	/** The text element. */
	protected Element textElement;

	/** The facade. */
	protected AbstractFacade facade;

	/** The change handler that is used to re-insert the document after a change. */
	private ChangeHandler ch;

	/**
	 * Instantiates a new abstract doc range.
	 * 
	 * @param textElement
	 *            the text element
	 * @param facade
	 *            the facade
	 * @param ch
	 *            the change handler
	 */
	public AbstractDocRange(Element textElement, AbstractFacade facade,
			ChangeHandler ch) {
		super(textElement);
		this.textElement = textElement;
		this.facade = facade;
		this.ch = ch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.StringListener#react(boolean, java.lang.String)
	 */
	public abstract void react(boolean hasUnsavedChanges, String s);
	
	/* (non-Javadoc)
	 * @see facade.StringListener#getType()
	 */
	@Override
	public int getType() {
		return StringListener.BLOCK;
	}

	/**
	 * Gets the attribute set from the text element.
	 * 
	 * @return the SimpleAttributeSet
	 */
	protected SimpleAttributeSet getAtts() {
		AttributeSet a = textElement.getAttributes();
		return new SimpleAttributeSet(a);
	}

	/**
	 * Gets the facade.
	 * 
	 * @return the facade
	 */
	public AbstractFacade getFacade() {
		return facade;
	}

	/**
	 * Gets the ID.
	 * 
	 * @return the ID
	 */
	public int getID() {
		return facade.getID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see block.Range#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + facade.getName();
	}

	/**
	 * Reset everything.
	 */
	protected void resetEverything() {
		// it's pretty important to always remove DocRanges before
		// handling change or you can get listener echo
		removeDocRanges();
		ch.handleChange(getFacade().getName());
	}

	/**
	 * Removes the doc ranges.
	 */
	protected void removeDocRanges() {
		Thread removingThread = new Thread() {
			public void run() {
				Remover r = null;
				try {
					r = new Remover("AbstractDocRange::" + facade.getName());
					SwingUtilities.invokeAndWait(r);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (Main.debug1) {
					String resource = r == null ? "In AbstractDocRange...R IS NULL??? This should never happen."
							: r.getName();
					System.out.println("Finished Removing Thread initiated by"
							+ resource + " on " + Thread.currentThread());

				}
			}
		};
		removingThread.start();
	}

	/**
	 * The Class Remover.
	 */
	class Remover implements Runnable {

		/** The name of the calling AbstractDocRange's facade. */
		private String name;

		/**
		 * Instantiates a new remover.
		 * 
		 * @param name
		 *            The name of the calling AbstractDocRange's facade
		 */
		public Remover(String name) {
			this.name = name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			facade.removeAll();
		}

		/**
		 * Gets the name.
		 * 
		 * @return The name of the calling AbstractDocRange's facade
		 */
		public String getName() {
			return name;
		}

	}

}

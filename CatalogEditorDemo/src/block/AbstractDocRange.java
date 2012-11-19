package block;

import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;

import facade.AbstractFacade;
import facade.StringListener;
import gui.Main;

public abstract class AbstractDocRange extends ConstantRange implements
		StringListener {
	protected Element textElement;
	protected AbstractFacade facade;
	private ChangeHandler ch;

	public AbstractDocRange(Element textElement, AbstractFacade facade,
			ChangeHandler ch) {
		super(textElement);
		this.textElement = textElement;
		this.facade = facade;
		this.ch = ch;
	}

	public abstract void react(boolean hasUnsavedChanges, String s);

	protected SimpleAttributeSet getAtts() {
		AttributeSet a = textElement.getAttributes();
		return new SimpleAttributeSet(a);
	}

	public AbstractFacade getFacade() {
		return facade;
	}

	public int getID() {
		return facade.getID();
	}

	@Override
	public String toString() {
		return super.toString() + facade.getName();
	}

	protected void resetEverything() {
		// it's pretty important to always remove DocRanges before
		// handling change or you can get listener echo
		removeDocRanges();
		ch.handleChange(getFacade().getName());
	}

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

	class Remover implements Runnable {
		private String name;

		public Remover(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			facade.removeAll();
		}

		public String getName() {
			return name;
		}

	}

}

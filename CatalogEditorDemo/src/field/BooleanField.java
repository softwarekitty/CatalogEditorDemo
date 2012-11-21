package field;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jdom2.Element;

import undecided.Util;
import facade.AbstractFacade;
import facade.StringListener;


/**
 * The Class BooleanField uses a JCheckBox to map the document value to the GUI.
 */
@SuppressWarnings("serial")
public class BooleanField extends JPanel implements ItemListener,
		StringListener {
	
	/** The check box. */
	private JCheckBox checkBox;
	
	/** The facade. */
	private AbstractFacade facade;

	/**
	 * Instantiates a new boolean field.
	 *
	 * @param facade the facade
	 * @param editors the editors
	 */
	public BooleanField(AbstractFacade facade,List<Element> editors) {
		this.facade = facade;
		checkBox = new JCheckBox(facade.getName().toLowerCase(), Boolean.parseBoolean(facade
				.getS()));
		checkBox.addItemListener(this);
		add(checkBox);
		if (!Util.editingIsAllowed(editors)) {
			checkBox.setEnabled(false);
		}
		facade.addStringListener(this);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent event) {

		if (event.getSource() == checkBox) {
			if (checkBox.isSelected()) {
				facade.setS(Boolean.TRUE.toString());
			} else {
				facade.setS(Boolean.FALSE.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see facade.StringListener#react(boolean, java.lang.String)
	 */
	@Override
	public void react(boolean hasUnsavedChanges, String s) {
		boolean checkBoxSelected = checkBox.isSelected();
		boolean sStringAsBoolean = Boolean.parseBoolean(s);
		if (checkBoxSelected != sStringAsBoolean) {
			checkBox.setSelected(sStringAsBoolean);
		}
		if (hasUnsavedChanges) {
			setBackground(Util.colorBook.getBackground(facade.getID()));
		} else {
			setBackground(UIManager.getColor("Panel.background"));
		}

	}

	/* (non-Javadoc)
	 * @see facade.StringListener#getType()
	 */
	@Override
	public int getType() {
		return StringListener.FIELD;
	}

}

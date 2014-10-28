package field;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import org.jdom2.Element;

import undecided.Util;
import facade.AbstractFacade;
import facade.OfferingFacade;
import facade.StringListener;
import gui.Main;

/**
 * The Class TermField is a special widget that maps a term element to the GUI.
 * It uses the 'yearsOffered' attribute to determine if an element should be
 * present or not as well as what its sandwich left and right should be. The 's'
 * value in this case is determined already by the ID.
 */
@SuppressWarnings("serial")
public class TermField extends JPanel implements StringListener, ActionListener {

	/** The facade. */
	protected OfferingFacade facade;

	/** The field label. */
	protected JLabel fieldLabel;

	/** The buttons. */
	private JRadioButton[] buttons;

	/** The selected index. */
	private int selectedIndex;

	/**
	 * Instantiates a new term field.
	 * 
	 * @param facade
	 *            the facade
	 * @param editors
	 *            the editors
	 */
	public TermField(OfferingFacade facade, List<Element> editors) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.facade = facade;

		// setup to make buttons
		String[] labels = OfferingFacade.yearsOfferedValues;
		buttons = new JRadioButton[labels.length];
		ButtonGroup bg = new ButtonGroup();
		boolean labeled = facade.getID() == AbstractFacade.SUMMER;
		boolean editingAllowed = Util.editingIsAllowed(editors);
		String yearsOffered = facade.getYearsOffered();

		// put a label on top indicating the term
		fieldLabel = new JLabel(facade.getS());
		if (!labeled) {
			fieldLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		} else {
			fieldLabel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
		}
		add(fieldLabel);

		// initialize a button for each yearsOffered value
		for (int i = 0; i < labels.length; i++) {

			// only Summer gets a label and should be on the right, to save
			// space
			String label = labeled ? labels[i] : "";
			JRadioButton b = new JRadioButton(label);
			b.addActionListener(this);
			buttons[i] = b;

			// set selected and enabled, add to group and this
			if (labels[i].equals(yearsOffered)) {
				b.setSelected(true);
				selectedIndex = i;
			}
			b.setEnabled(editingAllowed);
			if (!labeled) {
				b.setAlignmentX(Component.CENTER_ALIGNMENT);
			}
			bg.add(b);
			add(b);
		}
		facade.addStringListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.StringListener#react(boolean, java.lang.String)
	 */
	@Override
	public void react(boolean hasUnsavedChanges, String s) {
		if (Main.debug3) {
			System.out.println("in TermField.react with facade.yearsOffered: "
					+ facade.getYearsOffered() + " and selected button: "
					+ OfferingFacade.yearsOfferedValues[selectedIndex]);
		}
		if (!OfferingFacade.yearsOfferedValues[selectedIndex].equals(facade
				.getYearsOffered())) {
			selectedIndex = indexOfYearsOfferedValue(facade.getYearsOffered());
			buttons[selectedIndex].setSelected(true);
		}
		if (hasUnsavedChanges) {
			fieldLabel.setForeground(Color.black);
			setBackground(Util.colorBook.getBackground(facade.getID()));
		} else {
			fieldLabel.setForeground(Util.colorBook.getBackground(facade
					.getID()));
			setBackground(UIManager.getColor("Panel.background"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < OfferingFacade.yearsOfferedValues.length; i++) {
			if (Main.debug3) {
				System.out.println("checking button "
						+ OfferingFacade.yearsOfferedValues[i]);
			}
			if (buttons[i].isSelected()) {
				if (Main.debug3) {
					System.out.println("setting facade's yearsOffered to "
							+ OfferingFacade.yearsOfferedValues[i]);
				}
				facade.setYearsOffered(OfferingFacade.yearsOfferedValues[i]);
				selectedIndex = i;
				break;
			}
		}
	}

	/**
	 * Index of years offered value.
	 * 
	 * @param value
	 *            the value
	 * @return the int
	 */
	private int indexOfYearsOfferedValue(String value) {
		for (int i = 0; i < OfferingFacade.yearsOfferedValues.length; i++) {
			if (value.equals(OfferingFacade.yearsOfferedValues[i])) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facade.StringListener#getType()
	 */
	@Override
	public int getType() {
		return StringListener.FIELD;
	}

}

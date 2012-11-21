package facade;

import gui.widget.SaveButton;

import java.util.Collection;
import java.util.HashMap;

import org.jdom2.Element;

import undecided.Util;

/**
 * since there can be multiple views of a version, they must all synch with each
 * other as modified. That is to say, the variables in this facade should always
 * be identical to those in the views, and visa versa. This facade is meant to
 * make that and other tasks more straightforward.
 */
public class VersionFacade {
	/*
	 * Note: facadeMap and facadeList both point to the same facades At
	 * VersionFacade creation, all possibly necessary facades are created even
	 * if a version does not currently contain some elements. This greatly
	 * simplifies iterations over the facades needed by the listener management
	 * methods in AbstractFacade and text Element creation scheme in
	 * VersionDocument.insertVersion(). Facades are not added or removed after
	 * construction of this class.
	 */

	// for fetching
	/** The facade map. */
	private HashMap<Integer, AbstractFacade> facadeMap;

	// for iterating
	/** The facade list. */
	private Collection<AbstractFacade> facadeList;

	/** The designator string. */
	private String designatorString;

	/**
	 * Instantiates a new version facade.
	 * 
	 * @param version
	 *            the version element
	 * @param designator
	 *            the designator for the course (example: 'Com S 101')
	 * @param saveButton
	 *            the save button
	 */
	public VersionFacade(Element version, String designator,
			SaveButton saveButton) {
		designatorString = designator;

		facadeMap = new HashMap<Integer, AbstractFacade>();

		facadeMap.put(AbstractFacade.EXPERIMENTAL, new BooleanBlockFacade(
				version.getAttribute("experimental"), saveButton, this,
				AbstractFacade.EXPERIMENTAL, "X"));

		facadeMap.put(AbstractFacade.TITLE,
				new ElementFacade(version.getChild("TITLE"), saveButton, this,
						AbstractFacade.TITLE));

		facadeMap.put(AbstractFacade.DUAL,
				new SandwichFacade(version.getChild("DUAL"), saveButton, this,
						AbstractFacade.DUAL, "(Dual-listed with ", "). "));

		facadeMap.put(AbstractFacade.CROSS,
				new SandwichFacade(version.getChild("CROSS"), saveButton, this,
						AbstractFacade.CROSS, "(Cross-listed with ", "). "));

		facadeMap.put(AbstractFacade.PRIMARY, new ElementFacade(version
				.getChild("CONTACTHOURS").getChild("PRIMARY"), saveButton,
				this, AbstractFacade.PRIMARY));

		facadeMap.put(AbstractFacade.SECONDARY, new ElementFacade(version
				.getChild("CONTACTHOURS").getChild("SECONDARY"), saveButton,
				this, AbstractFacade.SECONDARY));

		facadeMap.put(AbstractFacade.CREDIT,
				new ElementFacade(version.getChild("CREDIT"), saveButton, this,
						AbstractFacade.CREDIT));

		facadeMap.put(AbstractFacade.REPEATABLE, new BooleanBlockFacade(version
				.getChild("CREDIT").getAttribute("repeatable"), saveButton,
				this, AbstractFacade.REPEATABLE, "Repeatable. "));

		facadeMap.put(AbstractFacade.MAXHOURS, new MaxHoursFacade(version
				.getChild("CREDIT").getAttribute("maxCreditCount"), saveButton,
				this, AbstractFacade.MAXHOURS));

		facadeMap.put(AbstractFacade.FALL,
				new OfferingFacade(version.getChild("OFFERED"), saveButton,
						this, AbstractFacade.FALL));

		facadeMap.put(AbstractFacade.SPRING,
				new OfferingFacade(version.getChild("OFFERED"), saveButton,
						this, AbstractFacade.SPRING));

		facadeMap.put(AbstractFacade.SUMMER,
				new OfferingFacade(version.getChild("OFFERED"), saveButton,
						this, AbstractFacade.SUMMER));

		facadeMap.put(AbstractFacade.PREREQ,
				new SandwichFacade(version.getChild("PREREQ"), saveButton,
						this, AbstractFacade.PREREQ, "Prereq: ", ". "));

		facadeMap.put(AbstractFacade.DESCRIPTION,
				new ElementFacade(version.getChild("DESCRIPTION"), saveButton,
						this, AbstractFacade.DESCRIPTION));

		facadeMap.put(AbstractFacade.NONMAJORGC, new BooleanBlockFacade(version
				.getChild("NOTES").getAttribute("nonMajorGraduateCredit"),
				saveButton, this, AbstractFacade.NONMAJORGC,
				"Nonmajor graduate credit. "));

		facadeMap.put(AbstractFacade.SFONLY,
				new BooleanBlockFacade(version.getAttribute("sfOnly"),
						saveButton, this, AbstractFacade.SFONLY,
						"Satisfactory-fail only. "));

		facadeMap.put(AbstractFacade.NOTES,
				new ElementFacade(version.getChild("NOTES"), saveButton, this,
						AbstractFacade.NOTES));

		facadeList = facadeMap.values();
	}

	/**
	 * Gets the facade with the given ID.
	 * 
	 * @param ID
	 *            the ID
	 * @return the abstract facade
	 */
	public AbstractFacade get(int ID) {
		return facadeMap.get(ID);
	}

	/**
	 * Gets the facade with the given name.
	 * 
	 * @param name
	 *            the name
	 * @return the abstract facade
	 */
	public AbstractFacade get(String name) {
		return get(Util.getIDFromName(name));
	}

	/**
	 * Gets the facade list.
	 * 
	 * @return the facade list
	 */
	public Collection<AbstractFacade> getFacadeList() {
		return facadeList;
	}

	/**
	 * Removes the all block string listeners.
	 */
	public void removeAllBlockStringListeners() {
		for (AbstractFacade a : facadeList) {
			a.removeBlockStringListener();
		}
	}

	/**
	 * Gets the designator.
	 * 
	 * @return the designator
	 */
	public String getDesignator() {
		return designatorString;
	}

	/**
	 * Checks for block listeners.
	 * 
	 * @return true, if any facade currently has a block listener
	 */
	public boolean hasBlockListeners() {
		for (AbstractFacade a : facadeList) {
			if (a.hasBlockListener()) {
				return true;
			}
		}
		return false;
	}
}

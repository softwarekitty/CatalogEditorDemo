package facade;

import gui.widget.SaveButton;

import java.util.List;

import org.jdom2.Element;

/**
 * To organize this conceptually, creation of a term is done through the yearsOffered variable,
 * but deletion can be done by setting that to none, or by trying to set S in the block view to 
 * something other than the term it should be.  There should never be a term element in the xml 
 * doc with yearsOffered="none" but if the term is not in the document, this facade should still 
 * exist and have that set to none.
 */
public class OfferingFacade extends AbstractFacade implements Sandwichable {
	
	/** The Constant yearsOfferedValues. */
	public static final String[] yearsOfferedValues = { "none", "odd", "even", "all" };
	
	/** The offering element. */
	private Element offeringElement;
	
	/** The years offered. */
	private String yearsOffered;

	/**
	 * Instantiates a new offering facade.
	 *
	 * @param offeringElement the offering element
	 * @param saveButton the save button
	 * @param vFacade the v facade
	 * @param ID the ID
	 */
	public OfferingFacade(Element offeringElement, SaveButton saveButton,
			VersionFacade vFacade, int ID) {
		super(saveButton, vFacade, ID);
		this.offeringElement = offeringElement;
		Element e = getTermElement();
		if (e != null) {
			yearsOffered = e.getAttribute("yearsOffered").getValue();
			if (!hasTerm()) {
				System.err
						.println("xml document has a term element with yearsOffered="+yearsOffered);
			}
		} else {
			setYearsOffered(yearsOfferedValues[0]);
		}
	}

	/**
	 * Checks for term.
	 *
	 * @return true, if successful
	 */
	private boolean hasTerm() {
		return !getYearsOffered().equals(yearsOfferedValues[0]);
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#getS()
	 */
	@Override
	public synchronized String getS() {
		switch (ID) {
		case FALL:
			return "F";
		case SPRING:
			return "S";
		case SUMMER:
			return "SS";
		default:
			return "___ERROR___";
		}
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#sync()
	 */
	@Override
	public void sync() {
		Element e = getTermElement();

		// if the facade says we have a term but there isn't one, put one there.
		if (hasTerm() && e == null) {
			Element term = new Element("TERM");
			term.setText(getS());
			term.setAttribute("yearsOffered", getYearsOffered());
			offeringElement.addContent(term);
		}
		// if the facade says we have a term and there is one, sync its
		// yearsOffered
		else if (hasTerm() && e != null) {
			e.setAttribute("yearsOffered", getYearsOffered());
		}
		// if the facade says we don't have a term but there is one, remove it
		else if (!hasTerm() && e != null) {
			offeringElement.removeContent(e);
		}
		// if the facade says we don't have one and we don't, do nothing
		handleChange();
	}

	/**
	 * Sets the years offered.
	 *
	 * @param yo the new years offered
	 */
	public void setYearsOffered(String yo) {
		yearsOffered = yo;
		handleChange();
	}

	/**
	 * Gets the years offered.
	 *
	 * @return the years offered
	 */
	public String getYearsOffered() {
		return yearsOffered;
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#facadeMatchesDocument()
	 */
	@Override
	public boolean facadeMatchesDocument() {
		Element e = getTermElement();
		// if there is no element for this term, then yearsOffered should be
		// "none"
		if (e == null) {
			return !hasTerm();

			// if there is an element, then yearsOffered should match the
			// attribute
		} else {
			return getYearsOffered().equals(
					e.getAttribute("yearsOffered").getValue());
		}
	}

	/*
	 * for a term, s has to match the term offered, so setting it to something
	 * else is the same as deleting the term
	 */
	/* (non-Javadoc)
	 * @see facade.AbstractFacade#setS(java.lang.String)
	 */
	@Override
	public synchronized void setS(String s) {
		if (!getS().equals(s)) {
			Element e = getTermElement();
			if (e != null) {
				offeringElement.removeContent(e);
			}
			setYearsOffered(yearsOfferedValues[0]);
		}
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#needsSyncing()
	 */
	@Override
	public boolean needsSyncing() {
		return termExists() ? hasUnsavedChanges : false;
	}

	/*
	 * this is the least efficient facade. Every time the save button checks all
	 * its syncables, this has to peek in at the children of the offering
	 * element and see if it contains a term for this one
	 */
	/**
	 * Gets the term element.
	 *
	 * @return the term element
	 */
	private Element getTermElement() {
		List<Element> offerings = offeringElement.getChildren();
		for (Element e : offerings) {
			if (e.getText().equals(getS())) {
				return e;
			}
		}
		return null;
	}

	/**
	 * Term exists.
	 *
	 * @return true, if successful
	 */
	public boolean termExists() {
		return getTermElement() != null;
	}

	/* (non-Javadoc)
	 * @see facade.Sandwichable#getLeft()
	 */
	public String getLeft() {
		return getYearsOffered().equals(yearsOfferedValues[3])?"":getYearsOffered()+" ";
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#isEmpty()
	 */
	public boolean isEmpty() {
		return !hasTerm();
	}

	/* (non-Javadoc)
	 * @see facade.Sandwichable#getRight()
	 */
	public String getRight() {
		return ". ";
	}

	/* (non-Javadoc)
	 * @see facade.AbstractFacade#toString()
	 */
	@Override
	public String toString() {
		if (hasTerm()) {
			return getS();
		} else {
			return "";
		}
	}
}

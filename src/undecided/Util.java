package undecided;

import facade.AbstractFacade;
import facade.VersionFacade;
import gui.Main;
import gui.widget.SaveButton;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import block.BlockPanel;


/**
 * The Class Util contains a lot of static utilities shared by different classes.
 */
//TODO - move all of these methods into the classes that call them, or...?
public class Util {
	
	/** The factory. */
	private static XPathFactory factory = XPathFactory.instance();
	
	/** The color book. */
	public static ColorBook colorBook = new ColorBook();
	
	/** The name book. */
	public static NameBook nameBook = new NameBook();

	/**
	 * Builds the document.
	 *
	 * @param catalogXMLFile the catalog xml file
	 * @return the document
	 */
	public static Document buildDocument(File catalogXMLFile) {
		try {
			SAXBuilder builder = new SAXBuilder();
			return builder.build(catalogXMLFile);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the element.
	 *
	 * @param elementPath the element path
	 * @return the element
	 */
	public static Element getElement(String elementPath) {
		if (Main.debug1) {
			System.out
					.println("in getElement with elementPath: " + elementPath);
		}
		XPathExpression<Element> xpath = factory.compile(elementPath,
				Filters.element());
		Element element = xpath.evaluateFirst(Main.getDocument());
		if (element == null) {
			System.err.println("document used: " + Main.getDocument()
					+ " elementPath: " + elementPath);
		}
		return element;
	}

	/**
	 * Gets the element.
	 *
	 * @param elementPath the element path
	 * @param fromThisElement the from this element
	 * @return the element
	 */
	public static Element getElement(String elementPath, Element fromThisElement) {
		if (Main.debug1) {
			System.out.println("in getElement #2 with elementPath: "
					+ elementPath
					+ "and using starting point of element with name: "
					+ fromThisElement.getName());
		}
		XPathExpression<Element> xpath = factory.compile(elementPath,
				Filters.element());
		Element element = xpath.evaluateFirst(fromThisElement);
		if (element == null) {
			System.err.println("document used: " + Main.getDocument()
					+ " elementPath: " + elementPath);
		}
		return element;
	}

	/**
	 * Gets the elements.
	 *
	 * @param elementsPath the elements path
	 * @return the elements
	 */
	public static LinkedList<Element> getElements(String elementsPath) {
		XPathExpression<Element> xpath = factory.compile(elementsPath,
				Filters.element());
		LinkedList<Element> elements = new LinkedList<Element>(
				xpath.evaluate(Main.getDocument()));
		if (elements.isEmpty()) {
			System.err.println("empty list...document used: "
					+ Main.getDocument() + " elementsPath: " + elementsPath);
		}
		return elements;
	}

	/**
	 * Editing is allowed.
	 *
	 * @param editors the editors
	 * @return true, if successful
	 */
	public static boolean editingIsAllowed(List<Element> editors) {
		Element editor = Main.getEditor();
		if (editor == null) {
			System.err.println("null editor in Util.editingIsAllowed");
			return false;
		}
		String currentEditorID = editor.getAttributeValue("netID");
		if (currentEditorID == null) {
			System.err
					.println("null netID from editor in Util.editingIsAllowed");
			return false;
		}
		if (editors == null) {
			return false;
		}
		for (Element e : editors) {
			String thisEditorID = e.getAttributeValue("netID");
			if (thisEditorID.equals(currentEditorID)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the current version from course.
	 *
	 * @param courseElement the course element
	 * @return the current version from course
	 */
	public static Element getCurrentVersionFromCourse(Element courseElement) {
		String programDesignator = courseElement.getParentElement()
				.getAttributeValue("designator");
		String courseNumber = courseElement.getAttributeValue("number");
		return getCurrentVersionFromCourse(courseElement, courseNumber,
				programDesignator);
	}

	/**
	 * Gets the current version from course.
	 *
	 * @param courseElement the course element
	 * @param courseNumber the course number
	 * @param programDesignator the program designator
	 * @return the current version from course
	 */
	public static Element getCurrentVersionFromCourse(Element courseElement,
			String courseNumber, String programDesignator) {
		String currentYear = Main.getDocument().getRootElement()
				.getAttributeValue("currentYear");
		String xPathExpression = "//PROGRAM[@designator=\"" + programDesignator
				+ "\"]/COURSE[@number=\"" + courseNumber
				+ "\"]/VERSION[@catalogYear=\"" + currentYear + "\"]";
		return getElement(xPathExpression);
	}

	/**
	 * Gets the editor elements with net id.
	 *
	 * @param netID the net id
	 * @return the editor elements with net id
	 */
	public static List<Element> getEditorElementsWithNetID(String netID) {
		return Util.getElements("//EDITOR[@netID=\"" + netID + "\"]");
	}

	/**
	 * Gets the name from id.
	 *
	 * @param ID the ID
	 * @return the name from id
	 */
	public static String getNameFromID(int ID) {
		switch (ID) {
		case AbstractFacade.CREDIT:
			return "CREDIT";
		case AbstractFacade.CROSS:
			return "CROSS";
		case AbstractFacade.DESCRIPTION:
			return "DESCRIPTION";
		case AbstractFacade.DUAL:
			return "DUAL";
		case AbstractFacade.EXPERIMENTAL:
			return "EXPERIMENTAL";
		case AbstractFacade.FALL:
			return "FALL";
		case AbstractFacade.MAXHOURS:
			return "MAXHOURS";
		case AbstractFacade.NONMAJORGC:
			return "NONMAJORGC";
		case AbstractFacade.NOTES:
			return "NOTES";
		case AbstractFacade.PREREQ:
			return "PREREQ";
		case AbstractFacade.PRIMARY:
			return "PRIMARY";
		case AbstractFacade.REPEATABLE:
			return "REPREATABLE";
		case AbstractFacade.SECONDARY:
			return "SECONDARY";
		case AbstractFacade.SFONLY:
			return "SFONLY";
		case AbstractFacade.SPRING:
			return "SPRING";
		case AbstractFacade.SUMMER:
			return "SUMMER";
		case AbstractFacade.TITLE:
			return "TITLE";
		default:
			return "___ERROR___";
		}
	}

	/**
	 * Gets the tree label.
	 *
	 * @param e the e
	 * @return the tree label
	 */
	public static String getTreeLabel(Element e) {
		String tagName = e.getName();
		if (tagName.equals("CATALOG")) {
			return catalogLabel(e);
		} else if (tagName.equals("EDITORS")) {
			return editorsLabel(e);
		} else if (tagName.equals("EDITOR")) {
			return editorLabel(e);
		} else if (tagName.equals("RESERVEDNUMBERS")) {
			return reserveNumbersLabel(e);
		} else if (tagName.equals("HEADER")) {
			return headerLabel(e);
		} else if (tagName.equals("COLLEGES")) {
			return collegesLabel(e);
		} else if (tagName.equals("COLLEGE")) {
			return collegeLabel(e);
		} else if (tagName.equals("PROGRAMS")) {
			return programsLabel(e);
		} else if (tagName.equals("PROGRAM")) {
			return programLabel(e);
		} else if (tagName.equals("COURSE")) {
			return courseLabel(e);
		} else if (tagName.equals("QUERIES")) {
			return queriesLabel(e);
		} else {
			return errorLabel();
		}
	}

	/**
	 * Catalog label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String catalogLabel(Element e) {
		return e.getAttributeValue("currentYear") + " Catalog";
	}

	/**
	 * Editors label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String editorsLabel(Element e) {
		return "editors for " + getTreeLabel(e.getParentElement());
	}

	/**
	 * Editor label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String editorLabel(Element e) {
		return "editor with netID: " + e.getAttributeValue("netID");
	}

	/**
	 * Reserve numbers label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String reserveNumbersLabel(Element e) {
		return "reserved course numbers";
	}

	/**
	 * Header label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String headerLabel(Element e) {
		Element parent = e.getParentElement();
		String parentName = parent.getName();
		if (parentName.equals("CATALOG")) {
			return "Catalog header";
		} else if (parentName.equals("COLLEGE")) {
			return parent.getAttributeValue("name") + " header";
		} else if (parentName.equals("PROGRAM")) {
			return parent.getAttributeValue("designator") + " header";
		} else {
			return errorLabel();
		}
	}

	/**
	 * Colleges label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String collegesLabel(Element e) {
		return "colleges";
	}

	/**
	 * College label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String collegeLabel(Element e) {
		return e.getAttributeValue("name") + " college";
	}

	/**
	 * Programs label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String programsLabel(Element e) {
		return "programs";
	}

	/**
	 * Program label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String programLabel(Element e) {
		return e.getAttributeValue("designator");
	}

	/**
	 * Queries label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String queriesLabel(Element e) {
		return "queries";
	}

	/**
	 * Course label.
	 *
	 * @param e the e
	 * @return the string
	 */
	private static String courseLabel(Element e) {
		Element currentVersion = getCurrentVersionFromCourse(e);
		Element parent = e.getParentElement();
		String experimentalString = Boolean.parseBoolean(currentVersion
				.getAttributeValue("experimental")) ? "X" : "";
		return parent.getAttributeValue("designator") + " "
				+ e.getAttributeValue("number") + experimentalString;
	}

	/**
	 * Error label.
	 *
	 * @return the string
	 */
	private static String errorLabel() {
		return "?";
	}

	/**
	 * Gets the ID from name.
	 *
	 * @param name the name
	 * @return the ID from name
	 */
	public static int getIDFromName(String name) {
		return nameBook.get(name) == null ? -1 : nameBook.get(name);
	}

	/**
	 * The Class Pair.
	 *
	 * @param <A> the generic type
	 * @param <B> the generic type
	 */
	public class Pair<A, B> {
		
		/** The first. */
		private A first;
		
		/** The second. */
		private B second;

		/**
		 * Instantiates a new pair.
		 *
		 * @param first the first
		 * @param second the second
		 */
		public Pair(A first, B second) {
			this.first = first;
			this.second = second;
		}

		/**
		 * Sets the first.
		 *
		 * @param first the new first
		 */
		public void setFirst(A first) {
			this.first = first;
		}

		/**
		 * Sets the second.
		 *
		 * @param second the new second
		 */
		public void setSecond(B second) {
			this.second = second;
		}

		/**
		 * Gets the first.
		 *
		 * @return the first
		 */
		public A getFirst() {
			return first;
		}

		/**
		 * Gets the second.
		 *
		 * @return the second
		 */
		public B getSecond() {
			return second;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object other) {
			if (other == null || !other.getClass().equals(getClass())) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Pair<A, B> p = (Pair<A, B>) other;

			if (!first.equals(p.first)) {
				return false;
			} else if (second.equals(p.second)) {
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Gets the default version.
	 *
	 * @return the default version
	 */
	public static Element getDefaultVersion(){
		String changeMe = "default value - please change me and all values!";
		Element newVersion = new Element("VERSION");
		newVersion.setAttribute("catalogYear",Main.getDocument().getRootElement().getAttributeValue("currentYear"));
		newVersion.setAttribute("experimental","false");
		newVersion.setAttribute("sfOnly","false");
		
		Element title = new Element("TITLE");
		title.setText(changeMe);
		
		Element primary = new Element("PRIMARY");
		primary.setText("3");
		
		Element secondary = new Element("SECONDARY");
		secondary.setText("0");
		
		Element cHours = new Element("CONTACTHOURS");
		cHours.addContent(primary);
		cHours.addContent(secondary);
		
		Element credit = new Element("CREDIT");
		credit.setAttribute("repeatable","false");
		credit.setAttribute("maxCreditCount","");
		credit.setText("3");
		
		Element notes = new Element("NOTES");
		notes.setAttribute("nonMajorGraduateCredit","false");
		
		
		newVersion.addContent(title);
		newVersion.addContent(new Element("DUAL"));
		newVersion.addContent(new Element("CROSS"));
		newVersion.addContent(cHours);
		newVersion.addContent(credit);
		newVersion.addContent(new Element("OFFERED"));
		newVersion.addContent(new Element("PREREQ"));
		newVersion.addContent(new Element("DESCRIPTION"));
		newVersion.addContent(notes);
		newVersion.addContent(new Element("COMMENTS"));
		return newVersion;
	}
	
	/**
	 * Gets the string from element.
	 *
	 * @param e the e
	 * @return the string from element
	 */
	public static String getStringFromElement(Element e) {
		String tagName = e.getName();
		if (tagName.equals("HEADER")) {
			return e.getText() + "\n\n";
		} else if (tagName.equals("COURSE")) {
			String programDesignator = e.getParentElement().getAttributeValue(
					"designator");
			String courseNumber = e.getAttributeValue("number");
			Element versionElement = Util.getCurrentVersionFromCourse(e);
			// passing in a dummy SaveButton and null editors should be ok as
			// long as the method is displaying uneditable text
			VersionFacade vFacade = new VersionFacade(versionElement,
					programDesignator + " " + courseNumber, new SaveButton());
			BlockPanel courseBlock = new BlockPanel(vFacade, null);
			return courseBlock.getText() + "\n\n";

			// because null returns are expected, it's easy to recurse the
			// containing folders
		} else if (tagName.equals("PROGRAMS") || tagName.equals("PROGRAM")
				|| tagName.equals("COLLEGES") || tagName.equals("COLLEGE")
				|| tagName.equals("CATALOG")) {
			String toReturn = "";
			List<Element> children = e.getChildren();
			for (Element child : children) {
				String childText = getStringFromElement(child);
				if (childText != null) {
					toReturn += childText;
				}
			}
			return toReturn.length() == 0 ? null : toReturn;
		}
		return null;
	}

	/*
	 * goes up to the root, adding all elements and their attribute values. In
	 * the current schema, this should be enough to uniquely identify a tree path,
	 * but would be ambiguous in a schema without unique attributes like number, etc.
	 */
	/**
	 * Gets the x path.
	 *
	 * @param selected the selected
	 * @return the x path
	 */
	public static String getXPath(Element selected) {
		String thisElement = selected.getName() + getAttributeString(selected);
		return getXPath(selected.getParentElement(),thisElement);
	}

	/**
	 * Gets the x path.
	 *
	 * @param selected the selected
	 * @param upperPath the upper path
	 * @return the x path
	 */
	private static String getXPath(Element selected,String upperPath){
		if(selected==null){
			return upperPath;
		}else{
			String thisElement = selected.getName() + getAttributeString(selected);
			return getXPath(selected.getParentElement(),thisElement+"/"+upperPath);
		}
		
	}

	/**
	 * Gets the attribute string.
	 *
	 * @param selected the selected
	 * @return the attribute string
	 */
	private static String getAttributeString(Element selected) {
		String toReturn = "";
		List<Attribute> allAttributes = selected.getAttributes();
		for (Attribute a : allAttributes) {
			toReturn += "[@" + a.getName() + "=\"" + a.getValue() + "\"]";
		}
		return toReturn;
	}
}

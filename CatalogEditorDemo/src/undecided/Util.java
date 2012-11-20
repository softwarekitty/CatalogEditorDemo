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

public class Util {
	private static XPathFactory factory = XPathFactory.instance();
	public static ColorBook colorBook = new ColorBook();
	public static NameBook nameBook = new NameBook();

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

	public static Element getCurrentVersionFromCourse(Element courseElement) {
		String programDesignator = courseElement.getParentElement()
				.getAttributeValue("designator");
		String courseNumber = courseElement.getAttributeValue("number");
		return getCurrentVersionFromCourse(courseElement, courseNumber,
				programDesignator);
	}

	public static Element getCurrentVersionFromCourse(Element courseElement,
			String courseNumber, String programDesignator) {
		String currentYear = Main.getDocument().getRootElement()
				.getAttributeValue("currentYear");
		String xPathExpression = "//PROGRAM[@designator=\"" + programDesignator
				+ "\"]/COURSE[@number=\"" + courseNumber
				+ "\"]/VERSION[@catalogYear=\"" + currentYear + "\"]";
		return getElement(xPathExpression);
	}

	public static List<Element> getEditorElementsWithNetID(String netID) {
		return Util.getElements("//EDITOR[@netID=\"" + netID + "\"]");
	}

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

	private static String catalogLabel(Element e) {
		return e.getAttributeValue("currentYear") + " Catalog";
	}

	private static String editorsLabel(Element e) {
		return "editors for " + getTreeLabel(e.getParentElement());
	}

	private static String editorLabel(Element e) {
		return "editor with netID: " + e.getAttributeValue("netID");
	}

	private static String reserveNumbersLabel(Element e) {
		return "reserved course numbers";
	}

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

	private static String collegesLabel(Element e) {
		return "colleges";
	}

	private static String collegeLabel(Element e) {
		return e.getAttributeValue("name") + " college";
	}

	private static String programsLabel(Element e) {
		return "programs";
	}

	private static String programLabel(Element e) {
		return e.getAttributeValue("designator");
	}

	private static String queriesLabel(Element e) {
		return "queries";
	}

	private static String courseLabel(Element e) {
		Element currentVersion = getCurrentVersionFromCourse(e);
		Element parent = e.getParentElement();
		String experimentalString = Boolean.parseBoolean(currentVersion
				.getAttributeValue("experimental")) ? "X" : "";
		return parent.getAttributeValue("designator") + " "
				+ e.getAttributeValue("number") + experimentalString;
	}

	private static String errorLabel() {
		return "?";
	}

	public static int getIDFromName(String name) {
		return nameBook.get(name) == null ? -1 : nameBook.get(name);
	}

	public class Pair<A, B> {
		private A first;
		private B second;

		public Pair(A first, B second) {
			this.first = first;
			this.second = second;
		}

		public void setFirst(A first) {
			this.first = first;
		}

		public void setSecond(B second) {
			this.second = second;
		}

		public A getFirst() {
			return first;
		}

		public B getSecond() {
			return second;
		}

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
	public static String getXPath(Element selected) {
		String thisElement = selected.getName() + getAttributeString(selected);
		return getXPath(selected.getParentElement(),thisElement);
	}

	private static String getXPath(Element selected,String upperPath){
		if(selected==null){
			return upperPath;
		}else{
			String thisElement = selected.getName() + getAttributeString(selected);
			return getXPath(selected.getParentElement(),thisElement+"/"+upperPath);
		}
		
	}

	private static String getAttributeString(Element selected) {
		String toReturn = "";
		List<Attribute> allAttributes = selected.getAttributes();
		for (Attribute a : allAttributes) {
			toReturn += "[@" + a.getName() + "=\"" + a.getValue() + "\"]";
		}
		return toReturn;
	}
}

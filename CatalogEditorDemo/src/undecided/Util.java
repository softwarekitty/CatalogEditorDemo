package undecided;

import gui.Main;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class Util {
	private static XPathFactory factory = XPathFactory.instance();
	public static ColorBook colorBook = new ColorBook();

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
		} else {
			return errorLabel();
		}
	}

	private static String catalogLabel(Element e) {
		return e.getAttributeValue("currentYear") + " Catalog";
	}

	private static String editorsLabel(Element e) {
		return errorLabel();
	}

	private static String editorLabel(Element e) {
		return e.getAttributeValue("netID");
	}

	private static String reserveNumbersLabel(Element e) {
		return "reserved course numbers";
	}

	private static String headerLabel(Element e) {
		Element parent = e.getParentElement();
		String parentName = parent.getName();
		if(parentName.equals("CATALOG")){
			return "Catalog header";
	    }else if (parentName.equals("COLLEGE")) {
			return parent.getAttributeValue("name") + " header";
		} else if (parentName.equals("PROGRAM")) {
			return parent.getAttributeValue("designator") + " header";
		} else {
			return errorLabel();
		}
	}

	private static String collegesLabel(Element e) {
		return errorLabel();
	}

	private static String collegeLabel(Element e) {
		return errorLabel();
	}

	private static String programsLabel(Element e) {
		return errorLabel();
	}

	private static String programLabel(Element e) {
		return e.getAttributeValue("designator");
	}

	private static String courseLabel(Element e) {
		Element parent = e.getParentElement();
		return parent.getAttributeValue("designator")+" "+e.getAttributeValue("number");
	}

	private static String errorLabel() {
		return "?";
	}
}

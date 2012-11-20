package block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import facade.AbstractFacade;
import facade.BooleanBlockFacade;
import facade.OfferingFacade;
import facade.Sandwichable;
import facade.VersionFacade;
import gui.Main;

@SuppressWarnings("serial")
public class VersionDocument extends DefaultStyledDocument {
	public static final String PARAM_WIDTH = "width";
	public static final String ELEMENT_NAME_VERSION = "version";
	public static final int OFFSET = 0;
	private static final String CONSTANT = "CONSTANT";
	private ArrayList<AbstractDocRange> ranges;
	private ArrayList<ConstantRange> constants;
	private ChangeHandler ch;
	private boolean dynamic;

	public VersionDocument(ChangeHandler ch,boolean dynamic) {
		this.ch = ch;
		this.dynamic = dynamic;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertVersion(VersionFacade facade) {
		try {
			SimpleAttributeSet attrs = new SimpleAttributeSet();
			ArrayList vSpecs = new ArrayList();
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));

			addConstant(facade.getDesignator(), vSpecs,
					AbstractFacade.EXPERIMENTAL);
			addContent(facade.get(AbstractFacade.EXPERIMENTAL), vSpecs);
			addConstant(". ", vSpecs, AbstractFacade.EXPERIMENTAL);
			addContent(facade.get(AbstractFacade.TITLE), vSpecs);
			addConstant(". ", vSpecs, AbstractFacade.TITLE);
			addSandwichedContent(facade.get(AbstractFacade.DUAL), vSpecs);
			addSandwichedContent(facade.get(AbstractFacade.CROSS), vSpecs);
			addConstant("(", vSpecs);
			addContent(facade.get(AbstractFacade.PRIMARY), vSpecs);
			addConstant("-", vSpecs);
			addContent(facade.get(AbstractFacade.SECONDARY), vSpecs);
			addConstant(") Cr. ", vSpecs);
			addContent(facade.get(AbstractFacade.CREDIT), vSpecs);
			addConstant(". ", vSpecs);
			addContent(facade.get(AbstractFacade.REPEATABLE), vSpecs);
			addSandwichedContent(facade.get(AbstractFacade.FALL), vSpecs);
			addSandwichedContent(facade.get(AbstractFacade.SPRING), vSpecs);
			addSandwichedContent(facade.get(AbstractFacade.SUMMER), vSpecs);
			addSandwichedContent(facade.get(AbstractFacade.PREREQ), vSpecs);
			addSandwichedContent(facade.get(AbstractFacade.MAXHOURS), vSpecs);
			addContent(facade.get(AbstractFacade.DESCRIPTION), vSpecs);
			addContent(facade.get(AbstractFacade.NONMAJORGC), vSpecs);
			addContent(facade.get(AbstractFacade.SFONLY), vSpecs);
			addContent(facade.get(AbstractFacade.NOTES), vSpecs);

			ElementSpec[] spec = new ElementSpec[vSpecs.size()];
			vSpecs.toArray(spec);

			this.insert(OFFSET, spec);

			// for dynamic docs, initialize the list of all document ranges
			if(dynamic){
				initializeRanges(getElements(), facade);
			}

		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

	// the sandwich filler must be added, even if empty, so that there is a
	// listener
	// present, but the sandwiching constants should only be added if there is a
	// filler
	@SuppressWarnings({ "rawtypes" })
	private void addSandwichedContent(AbstractFacade abstractFacade,
			ArrayList vSpecs) {
		Sandwichable sw = (Sandwichable) abstractFacade;
		if (!sw.isEmpty()) {
			addConstant(sw.getLeft(), vSpecs, abstractFacade.getID());
		}
		addContent(abstractFacade, vSpecs);
		if (!sw.isEmpty()) {
			addConstant(sw.getRight(), vSpecs, abstractFacade.getID());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addContent(AbstractFacade facade, ArrayList vSpecs) {
		String content = facade.toString();
		if (Main.debug3) {
			System.out.println("adding content with string:" + content
					+ " and s: " + facade.getS());
			int ID = facade.getID();
			if (ID == AbstractFacade.FALL || ID == AbstractFacade.SPRING
					|| ID == AbstractFacade.SUMMER) {
				OfferingFacade of = (OfferingFacade) facade;
				System.out.println(" yearsOffered: " + of.getYearsOffered());
			}
		}
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		attrs.addAttribute(ElementNameAttribute, facade.getName());
		if(dynamic){
			attrs.addAttribute(StyleConstants.Background, facade.getBlockColor());		
		}else{
			attrs.addAttribute(StyleConstants.Background, Color.WHITE);
		}
		if (facade.getID() == AbstractFacade.EXPERIMENTAL
				|| facade.getID() == AbstractFacade.TITLE) {
			attrs.addAttribute(StyleConstants.CharacterConstants.Bold,
					Boolean.TRUE);
		} else if (facade.getID() == AbstractFacade.PREREQ) {
			attrs.addAttribute(StyleConstants.CharacterConstants.Italic,
					Boolean.TRUE);
		}
		attrs.addAttribute(PARAM_WIDTH, new Integer(content.length()));

		ElementSpec start = new ElementSpec(attrs, ElementSpec.StartTagType);
		vSpecs.add(start);
		ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(),
				ElementSpec.ContentType, content.toCharArray(), 0,
				content.length());
		vSpecs.add(parContent);
		ElementSpec end = new ElementSpec(attrs, ElementSpec.EndTagType);
		vSpecs.add(end);
	}

	@SuppressWarnings({ "rawtypes" })
	private void addConstant(String content, ArrayList vSpecs) {
		addConstant(content, vSpecs, -1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addConstant(String content, ArrayList vSpecs, int ID) {
		if ("".equals(content)) {
			return;
		}
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		attrs.addAttribute(ElementNameAttribute, CONSTANT);
		attrs.addAttribute(StyleConstants.Background, Color.WHITE);
		if (ID == AbstractFacade.EXPERIMENTAL) {
			attrs.addAttribute(StyleConstants.CharacterConstants.Bold,
					Boolean.TRUE);
		} else if (ID == AbstractFacade.PREREQ) {
			attrs.addAttribute(StyleConstants.CharacterConstants.Italic,
					Boolean.TRUE);
		}
		attrs.addAttribute(PARAM_WIDTH, new Integer(content.length()));

		ElementSpec start = new ElementSpec(attrs, ElementSpec.StartTagType);
		vSpecs.add(start);
		ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(),
				ElementSpec.ContentType, content.toCharArray(), 0,
				content.length());
		vSpecs.add(parContent);
		ElementSpec end = new ElementSpec(attrs, ElementSpec.EndTagType);
		vSpecs.add(end);
	}

	private void initializeRanges(LinkedList<Element> textElements,
			VersionFacade facade) {
		ranges = new ArrayList<AbstractDocRange>(20);
		constants = new ArrayList<ConstantRange>();
		for (Element e : textElements) {
			// if(Main.debug3){
			// System.out.println("e: "+e.getName());
			// }
			AbstractFacade aFacade = facade.get(e.getName());

			// can be null for paragraphs, content elements, etc.
			if (aFacade != null) {
				AbstractDocRange dr = createRange(e, aFacade);
				aFacade.addStringListener(dr);
				if (Main.debug3) {
					System.out.println("added block listener to aFacade:"
							+ aFacade.getName());
				}
				ranges.add(dr);
			} else if (e.getName().equals(CONSTANT)) {
				ConstantRange cr = new ConstantRange(e);
				constants.add(cr);
			}
		}
	}

	/*
	 * when a change in the VersionDocument happens, this method determines what
	 * DocRanges overlap the change range and then sets their facade(s) to match
	 * the new state
	 */
	public void notifyFacade(DocumentEvent e) {
		boolean insert = e.getType() == DocumentEvent.EventType.INSERT;

		// first create a change range from the DocumentEvent
		int length = e.getType() == DocumentEvent.EventType.REMOVE ? -e
				.getLength() : e.getLength();
		Range changeRange = new Range(e.getOffset(), length + e.getOffset());

		// iterate through all the ranges to see which ones overlap with the
		// change range
		for (AbstractDocRange d : ranges) {
			if (!d.getText().equals(d.getFacade().toString())) {
				setFacadeValue(d);

				// here is a special case where you're trying to insert at the
				// beginning of a range
				// but because you inserted, you moved the range over and are
				// not overlapping it,
				// strictly speaking, so set the text to the new combined range.
			} else if (insert && changeRange.end == d.start()
					&& notBooleanBlock(d.getID())) {
				d.getFacade().removeBlockStringListener();
				d.getFacade().setS(
						getRangeText(new Range(changeRange.start(), d.end())));
			}
		}
	}

	private String getRangeText(Range changeRange) {
		try {
			return this.getText(changeRange.start(), changeRange.end()
					- changeRange.start());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setFacadeValue(AbstractDocRange d) {
		if (notBooleanBlock(d.getID())) {
			if (Main.debug3) {
				System.out.println("setting facade with name: "
						+ d.getFacade().getName() + " and text(s): "
						+ d.getFacade().getS() + " to text: " + d.getText());
			}
			d.getFacade().removeBlockStringListener();
			d.getFacade().setS(d.getText());
		} else {
			BooleanBlockFacade df = (BooleanBlockFacade) d.getFacade();
			df.removeBlockStringListener();
			df.setS(Boolean.FALSE.toString());
		}
	}

	private boolean isSandwich(int ID) {
		switch (ID) {
		case AbstractFacade.CROSS:
		case AbstractFacade.DUAL:
		case AbstractFacade.PREREQ:
		case AbstractFacade.MAXHOURS:
		case AbstractFacade.FALL:
		case AbstractFacade.SPRING:
		case AbstractFacade.SUMMER:
			return true;
		default:
			return false;
		}
	}

	private boolean notBooleanBlock(int ID) {
		switch (ID) {
		case AbstractFacade.TITLE:
		case AbstractFacade.PRIMARY:
		case AbstractFacade.SECONDARY:
		case AbstractFacade.DESCRIPTION:
		case AbstractFacade.NOTES:
		case AbstractFacade.CREDIT:
			return true;
		default:
			return isSandwich(ID);
		}
	}

	private AbstractDocRange createRange(Element e, AbstractFacade facade) {
		if (notBooleanBlock(facade.getID())) {
			return new DocRange(e, facade, ch);
		} else {
			return new BooleanDocRange(e, (BooleanBlockFacade) facade, ch);
		}
	}

	public boolean overlapsAConstantRange(Range changeRange) {
		for (ConstantRange c : constants) {
			if (c.overlaps(changeRange)) {
				if (Main.debug3) {
					System.out.println("constantRange with text: "
							+ c.getText() + " is overlapped");
				}
				return true;
			}
		}
		return false;
	}

	public Element getFirstElementWithName(String name) {
		LinkedList<Element> list = getElements();
		for (Element e : list) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		System.err.println("In getFirstElementWithName and name:" + name
				+ " - this element was not found");
		System.err.println("Here are the names of all the elements:");
		for (Element e : list) {
			System.err.println(e.getName());
		}
		return null;
	}

	public LinkedList<Element> getElements() {

		// add all children to a list using recursive helper method
		Element root = getDefaultRootElement();
		int nChildren = root.getElementCount();
		LinkedList<Element> allChildren = new LinkedList<Element>();
		int cFound = addAllChildrenToList(allChildren, nChildren, root);

		// recursive algorithm should return size of list it populates
		if (Main.debug3 && cFound != allChildren.size()) {
			System.err.println("number of children found is: " + cFound
					+ " but list length is: " + allChildren.size());
		}
		return allChildren;
	}

	// a helper method that adds all elements to a list and counts how many it
	// adds
	private int addAllChildrenToList(LinkedList<Element> allChildren,
			int nChildren, Element thisRoot) {
		if (nChildren == 0) {
			return 0;
		} else {
			int toReturn = 0;
			for (int i = 0; i < nChildren; i++) {
				Element child = thisRoot.getElement(i);
				allChildren.add(child);
				toReturn++;
				toReturn += addAllChildrenToList(allChildren,
						child.getElementCount(), child);
			}
			return toReturn;
		}
	}
}

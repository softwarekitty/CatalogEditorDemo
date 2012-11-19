package block;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import facade.StringFacade;
import facade.VersionFacade;
import gui.Main;

@SuppressWarnings("serial")
public class VersionDocument extends DefaultStyledDocument {
	public static final String PARAM_WIDTH = "width";
	public static final String ELEMENT_NAME_VERSION = "version";
	public static final int OFFSET = 0;
	private ArrayList<DocRange> ranges;
	private ChangeHandler ch;

	public VersionDocument(ChangeHandler ch) {
		this.ch = ch;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void insertVersion(VersionFacade facade) {
		try {
			SimpleAttributeSet attrs = new SimpleAttributeSet();

			ArrayList vSpecs = new ArrayList();
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			ElementSpec tab = new ElementSpec(new SimpleAttributeSet(),
					ElementSpec.ContentType, "\t\t".toCharArray(), 0, 2);
			vSpecs.add(tab);
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			addElement(facade.getFacade("TITLE"), vSpecs);
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			ElementSpec newline = new ElementSpec(new SimpleAttributeSet(),
					ElementSpec.ContentType, "\n".toCharArray(), 0, 1);
			vSpecs.add(newline);
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			addElement(facade.getFacade("DESCRIPTION"), vSpecs);
			addElement(facade.getFacade("PREREQ"), vSpecs);

			ElementSpec[] spec = new ElementSpec[vSpecs.size()];
			vSpecs.toArray(spec);

			this.insert(OFFSET, spec);

			// initialize the list of all document ranges
			initializeRanges(getElements(), facade);
			
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addElement(StringFacade facade, ArrayList vSpecs) {
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		attrs.addAttribute(ElementNameAttribute, facade.getName());
		attrs.addAttribute(StyleConstants.Background,
				facade.getBlockColor());
		String content = facade.getS();
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
	
	public void notifyFacade(DocumentEvent e){
		// TODO - use a sparse array, store ranges there for binary search
		int length = e.getLength();
		if(e.getType()==DocumentEvent.EventType.REMOVE){
			length = -length;
		}
		if (Main.debug3) {
			System.out.println();
			System.out.println("...in VersionDocument.notifyFacade(), e.offset: "
					+ e.getOffset() + " e...length: " + length);
		}


		Range changeRange = new Range(e.getOffset(), length
				+ e.getOffset());
		// can be null before initialized
		for (DocRange d : ranges) {
			String dName = d.getFacade().getName();
			if (Main.debug3) {
				System.out.println("d.name: " + dName
						+ " d.start(): " + d.start() + " d.end(): " + d.end()+" d.getText(): "+d.getText());
				System.out.println("changeRange...start:"+changeRange.start()+" end: "+changeRange.end()+" overlaps: "+d.overlaps(changeRange));
			}
			if (d.overlaps(changeRange)) {
				if (Main.debug3) {
					System.out.println("d overlaps changeRange, so setting: " + dName
							+ " facade's string to text: " + d.getText());

				}
				d.getFacade().removeStringListener(d);
				if(Main.debug3){
					System.out.println(" removed "+ dName + " DocRange from StringFacade...contains it?"+d.getFacade().containsListener(d));
				}
				d.getFacade().setS(d.getText());
			}

		}
	}


	private void initializeRanges(LinkedList<Element> textElements,
			VersionFacade facade) {
		ranges = new ArrayList<DocRange>();
		for (Element e : textElements) {

			// requires a 1-1 relationship between elements and names
			StringFacade sFacade = facade.getFacade(e.getName());
			// can be null for paragraphs, content elements, etc.
			if (sFacade != null) {
				DocRange dr = new DocRange(e, sFacade,ch);
				sFacade.addStringListener(dr);
				ranges.add(dr);
			}
		}
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

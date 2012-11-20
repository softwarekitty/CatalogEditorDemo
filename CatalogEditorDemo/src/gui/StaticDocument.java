package gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.jdom2.Element;

import undecided.Util;
import block.VersionDocument;
import block.VersionEditorKit;
import facade.VersionFacade;
import gui.widget.SaveButton;

@SuppressWarnings("serial")
public class StaticDocument extends DefaultStyledDocument{
	private Element root;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StaticDocument(Element root){
		if(Main.debug3){
			System.out.println("constructing static document");
		}
		this.root=root;
		ArrayList vSpecs = getVSpecsFromElement(root);
		ElementSpec[] spec = new ElementSpec[vSpecs.size()];
		vSpecs.toArray(spec);
		try {
			insert(VersionDocument.OFFSET, spec);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<ElementSpec> getVSpecsFromElement(Element e) {
		ArrayList<ElementSpec> vSpecs = new ArrayList<ElementSpec>();
		String tagName = e.getName();
		if(Main.debug3){
			System.out.println("getting vspecs from element with tagname: "+tagName);
		}
		
		if (tagName.equals("HEADER")) {
			SimpleAttributeSet attrs = new SimpleAttributeSet();
			if(root.getName().equals(e.getParentElement().getName())){
				vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
				vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			}
			
			String content = e.getText();
			SimpleAttributeSet textAttrs = new SimpleAttributeSet();
			textAttrs.addAttribute(StyleConstants.Background, Color.WHITE);
			textAttrs.addAttribute(ElementNameAttribute, tagName);
			textAttrs.addAttribute(VersionDocument.PARAM_WIDTH, new Integer(content.length()));
			ElementSpec start = new ElementSpec(textAttrs, ElementSpec.StartTagType);
			vSpecs.add(start);
			ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(),
					ElementSpec.ContentType, content.toCharArray(), 0,
					content.length());
			vSpecs.add(parContent);
			ElementSpec end = new ElementSpec(textAttrs, ElementSpec.EndTagType);
			vSpecs.add(end);
			
			//add a separator
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			vSpecs.add(new ElementSpec(new SimpleAttributeSet(),
					ElementSpec.ContentType, "\n".toCharArray(), 0, 1));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			
			return vSpecs;
		} else if (tagName.equals("COURSE")) {
			
			String programDesignator = e.getParentElement().getAttributeValue(
					"designator");
			String courseNumber = e.getAttributeValue("number");
			Element versionElement = Util.getCurrentVersionFromCourse(e);
			
			// passing in a dummy SaveButton and should be ok as
			// since this facade will go out of scope once the ArrayList is returned			
			VersionFacade vFacade = new VersionFacade(versionElement,
					programDesignator + " " + courseNumber, new SaveButton());
			
			// create pane
			JEditorPane edit = new JEditorPane();
			edit.setEditorKit(new VersionEditorKit(null,false));
			VersionDocument doc = ((VersionDocument)edit.getDocument());
			doc.insertVersion(vFacade);
			LinkedList<javax.swing.text.Element> list = doc.getElements();
			for(javax.swing.text.Element textElement:list){
				String elementName = textElement.getName();
				String content ="";
				if(!(elementName.equals("paragraph")||elementName.equals("content"))){
					try {
						content = doc.getText(textElement.getStartOffset(), textElement.getEndOffset()-textElement.getStartOffset());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
				AttributeSet textAttrs = textElement.getAttributes();				
				vSpecs.add(new ElementSpec(textAttrs, ElementSpec.StartTagType));
				ElementSpec parContent = new ElementSpec(new SimpleAttributeSet(),
						ElementSpec.ContentType, content.toCharArray(), 0,
						content.length());
				vSpecs.add(parContent);
				vSpecs.add(new ElementSpec(textAttrs, ElementSpec.EndTagType));


			}	
			
			//add a separator
			SimpleAttributeSet attrs = new SimpleAttributeSet();
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			vSpecs.add(new ElementSpec(new SimpleAttributeSet(),
					ElementSpec.ContentType, "\n".toCharArray(), 0, 1));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.EndTagType));
			vSpecs.add(new ElementSpec(attrs, ElementSpec.StartTagType));
			
			return vSpecs;
			
		} else if (tagName.equals("PROGRAMS") || tagName.equals("PROGRAM")
				|| tagName.equals("COLLEGES") || tagName.equals("COLLEGE")
				|| tagName.equals("CATALOG")) {

			List<Element> children = e.getChildren();
			for (Element child : children) {
				vSpecs.addAll(getVSpecsFromElement(child));
			}
		}
		return vSpecs;
	}

}
